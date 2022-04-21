package com.programm.ioutils.io.console.formatters;

import com.programm.ioutils.io.api.IFormatter;

public class ReplaceArgsFormatter implements IFormatter {

    private final String open;
    private final String close;

    public ReplaceArgsFormatter(String open, String close) {
        this.open = open;
        this.close = close;
    }

    @Override
    public String format(String message, Object... args) {
        if(message == null || args == null || args.length == 0) return message;

        StringBuilder sb = new StringBuilder();

        int msgLen = message.length();
        int curArgIndex = 0;
        int last = 0;
        outerLoop:
        for(int i=0;i<msgLen;i++){
            if(message.startsWith(open, i)){
                int p = i + open.length();
                for(;p<msgLen;p++){
                    if(!Character.isDigit(message.charAt(p))) break;
                    else if(p + 1 == msgLen) break outerLoop;
                }

                if(!message.startsWith(close, p)) {
                    i = p - 1;
                    continue;
                }

                int num;
                boolean curArgs = false;
                if(p == i + open.length()){
                    num = curArgIndex;
                    curArgIndex++;
                    curArgs = true;
                }
                else {
                    String _num = message.substring(i + open.length(), p);
                    num = Integer.parseInt(_num);
                }

                if(num < 0 || num >= args.length){
                    if(curArgs) curArgIndex--;
                    i = p + close.length() - 1;
                    continue;
                }

                sb.append(message, last, i);
                sb.append(args[num]);

                i = p + close.length() - 1;
                last = i + 1;
            }
        }

        sb.append(message, last, msgLen);

        return sb.toString();
    }
}
