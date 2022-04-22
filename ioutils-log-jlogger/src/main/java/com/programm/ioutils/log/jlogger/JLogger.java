package com.programm.ioutils.log.jlogger;

import com.programm.ioutils.io.api.IOutput;
import com.programm.ioutils.log.api.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JLogger extends LevelLogger implements IConfigurableLogger {

    private static final String DATE_FORMAT = "yyyy/MM/dd";
    private static final String TIME_FORMAT = "HH:mm:ss";

    private static int getMaxLen(String... keys){
        int max = -1;

        for(String key : keys){
            max = Math.max(max, key.length());
        }

        return max;
    }

    private static class PkgLvlNode {
        private final Map<String, PkgLvlNode> children = new HashMap<>();
        private Integer level;
        private boolean passes;

        public PkgLvlNode(Integer level, boolean passes) {
            this.level = level;
            this.passes = passes;
        }
    }

    private static class FormattedDateStringSupplier {
        private final String format;
        private String dateString;

        public FormattedDateStringSupplier(String format) {
            this.format = format;
        }

        @Override
        public String toString() {
            if(dateString == null){
                DateFormat dateFormat = new SimpleDateFormat(format);
                dateString = dateFormat.format(new Date());
            }

            return dateString;
        }
    }

    private final Map<String, Object> formatArgs = new HashMap<>();
    private static final int FORMAT_ARGS_MAX_LENGTH = getMaxLen("MSG", "LVL", "LOG", "CLS", "MET", "THREAD", "DATE", "TIME");

    private final Map<String, PkgLvlNode> pkgLvlNodes = new HashMap<>();
    private final Map<String, Integer> logNameLevels = new HashMap<>();
    private IOutput output = new DefaultConsoleOut();
    private String format;

    private Class<?> nextLogInfoCls;
    private String nextLogInfoMethodName;

    @Override
    protected void log(String s, int level, Object... args) {
        String logName = null;
        String pkgName = null;
        String cName = null;
        String mName = null;
        Thread curThread = Thread.currentThread();
        String tName = curThread.getName();

        if(nextLogInfoCls != null){
            Logger loggerAnnotation = nextLogInfoCls.getAnnotation(Logger.class);

            if (loggerAnnotation != null) {
                String oLogName = loggerAnnotation.value();

                if (oLogName.isEmpty()) {
                    oLogName = loggerAnnotation.name();
                }

                if (!oLogName.isEmpty()) {
                    logName = oLogName;
                }
            }

            pkgName = nextLogInfoCls.getPackageName();
            cName = nextLogInfoCls.getSimpleName();
            mName = nextLogInfoMethodName;

            nextLogInfoCls = null;
            nextLogInfoMethodName = null;
        }
        else {
            StackTraceElement[] callers = curThread.getStackTrace();

            for(int i=callers.length-2;i>1;i--) {
                StackTraceElement caller = callers[i];
                String curMethodName = caller.getMethodName();

                if (curMethodName.equals("trace") || curMethodName.equals("debug") || curMethodName.equals("info") || curMethodName.equals("warn") || curMethodName.equals("error")) {
                    String fullClsName = callers[i + 1].getClassName();
                    String methodName = callers[i + 1].getMethodName();
                    try {
                        Class<?> cls = JLogger.class.getClassLoader().loadClass(fullClsName);
                        String clsName = cls.getSimpleName();
                        Logger loggerAnnotation = cls.getAnnotation(Logger.class);

                        if (loggerAnnotation != null) {
                            String oLogName = loggerAnnotation.value();

                            if (oLogName.isEmpty()) {
                                oLogName = loggerAnnotation.name();
                            }

                            if (!oLogName.isEmpty()) {
                                logName = oLogName;
                            }
                        }

                        pkgName = cls.getPackageName();
                        cName = clsName;
                        mName = methodName;
                        break;
                    } catch (ClassNotFoundException ignore) {}
                }
            }

            if(pkgName == null) {
                throw new IllegalStateException("Could not find caller method of logger!");
            }
        }

        int logLevel = calcLogLevel(pkgName, logName);
        if(logLevel > level) return;

        String _level = ILogger.levelToString(level);

        String message = StringUtils.prepareMessage(s, args);
        if(format != null) {
            putArgs(message, _level, logName, cName, mName, tName);
            message = StringUtils.format(format, formatArgs, FORMAT_ARGS_MAX_LENGTH);
        }

        output.println(message);
    }

    private void putArgs(String msg, String lvl, String log, String cls, String met, String thread){
        formatArgs.put("MSG", msg);
        formatArgs.put("LVL", lvl);
        formatArgs.put("LOG", log);
        formatArgs.put("CLS", cls);
        formatArgs.put("MET", met);
        formatArgs.put("THREAD", thread);
        formatArgs.put("DATE", new FormattedDateStringSupplier(DATE_FORMAT));
        formatArgs.put("TIME", new FormattedDateStringSupplier(TIME_FORMAT));
    }

    private int calcLogLevel(String packageName, String logName) {
        if(logName != null){
            Integer level = logNameLevels.get(logName);
            if(level != null) return level;
        }

        int level = level();

        String[] pkgs = packageName.split("\\.");

        Map<String, PkgLvlNode> nodes = pkgLvlNodes;
        int index = 0;
        while(index < pkgs.length){
            String pkg = pkgs[index];
            PkgLvlNode node = nodes.get(pkg);

            if(node == null) break;
            if(node.level != null){
                if(node.passes || index + 1 == pkgs.length){
                    level = node.level;
                }
            }

            nodes = node.children;
            index++;
        }

        return level;
    }

    public JLogger level(int level) throws LoggerConfigException {
        super.level(level);
        return this;
    }

    @Override
    public void setNextLogInfo(Class<?> cls, String methodName) {
        this.nextLogInfoCls = cls;
        this.nextLogInfoMethodName = methodName;
    }

    @Override
    public JLogger format(String format) throws LoggerConfigException {
        this.format = format;
        return this;
    }

    @Override
    public JLogger packageLevel(String pkg, int level) throws LoggerConfigException {
        setPkgLevel(pkgLvlNodes, pkg.split("\\."), 0, level);
        return this;
    }

    @Override
    public IConfigurableLogger logNameLevel(String name, int level) throws LoggerConfigException {
        logNameLevels.put(name, level);
        return this;
    }

    @Override
    public JLogger output(IOutput output) throws LoggerConfigException {
        this.output = output;
        return this;
    }

    private void setPkgLevel(Map<String, PkgLvlNode> nodes, String[] pkg, int index, int level){
        boolean passes = false;
        boolean end = false;

        if(index + 2 == pkg.length && pkg[index + 1].equals("*")){
            passes = true;
            end = true;
        }
        else if(index + 1 == pkg.length){
            end = true;
        }

        PkgLvlNode node = nodes.get(pkg[index]);
        if(end){
            if(node == null){
                nodes.put(pkg[index], new PkgLvlNode(level, passes));
            }
            else {
                node.level = level;
                if(passes) {
                    node.passes = true;
                }
            }
        }
        else {
            if(node == null){
                node = new PkgLvlNode(null, false);
                nodes.put(pkg[index], node);
            }

            setPkgLevel(node.children, pkg, index + 1, level);
        }
    }
}
