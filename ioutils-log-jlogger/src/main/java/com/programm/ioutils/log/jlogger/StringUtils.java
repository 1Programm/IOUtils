package com.programm.ioutils.log.jlogger;

import java.util.Map;

class StringUtils {

    public static String replaceArgs(String message, String replaceStart, String replaceEnd, Object... args) {
        if(message == null || args == null || args.length == 0) return message;

        StringBuilder sb = new StringBuilder();

        int i=0;
        int last = 0;
        int index;

        while((index = message.indexOf(replaceStart, last)) != -1){
            sb.append(message, last, index);

            int replaceEndIndex = message.indexOf(replaceEnd, index + 1);
            if(replaceEndIndex == -1) break;

            last = index;

            if(index + replaceStart.length() + 1 < replaceEndIndex) {
                String between = message.substring(index + replaceStart.length(), replaceEndIndex);

                try {
                    int num = Integer.parseInt(between);
                    if(num >= args.length) continue;

                    if(args[num] != null) {
                        sb.append(args[num].toString());
                    }
                    else {
                        sb.append("null");
                    }
                }
                catch (NumberFormatException e){
                    continue;
                }
            }
            else {
                if(i >= args.length) break;

                if(args[i] != null) {
                    sb.append(args[i].toString());
                }
                else {
                    sb.append("null");
                }
                i++;
            }

            last = replaceEndIndex + replaceEnd.length();
        }

        sb.append(message, last, message.length());

        return sb.toString();
    }

    public static String prepareMessage(String s, Object... args){
        StringBuilder sb = new StringBuilder();

        int argsIndex = 0;
        int last = 0;

        for(int i=0;i<s.length();i++){
            char c = s.charAt(i);

            if(c == '{'){
                int openBracket = i;
                i++;
                StringBuilder _num = new StringBuilder();
                while(i < s.length()){
                    c = s.charAt(i);
                    if(!Character.isDigit(c)) break;
                    _num.append(c);
                    i++;
                }

                if(c == '}'){
                    Integer num = null;
                    if(_num.length() != 0){
                        num = Integer.parseInt(_num.toString());
                    }

                    if(num == null){
                        if(argsIndex < args.length){
                            sb.append(s, last, openBracket);
                            sb.append(args[argsIndex++]);
                            last = i + 1;
                        }
                    }
                    else {
                        if(num < args.length){
                            sb.append(s, last, openBracket);
                            sb.append(args[num]);
                            last = i + 1;
                        }
                    }
                }
            }
        }

        sb.append(s, last, s.length());

        return sb.toString();
    }

    public static String format(String format, Map<String, Object> args, int maxArgKeyLength) {
        return format(format, 0, format.length(), args, maxArgKeyLength);
    }

    private static String format(String format, int start, int end, Map<String, Object> args, int maxArgKeyLength) {
        StringBuilder sb = new StringBuilder(Math.max(end - start, 16));

        int last = start;
        outerLoop:
        for(int i=start;i<end;i++){
            char c = format.charAt(i);

            if(c == '$'){
                Object value = null;
                String key = null;
                for(int o=Math.min(i+1+maxArgKeyLength, end);o>i;o--){
                    key = format.substring(i+1, o);
                    value = args.get(key);
                    if(value != null) break;
                    if(o - 1 == i) break;
                }

                if(value != null){
                    sb.append(format, last, i);
                    i += key.length();
                    i = appendValueOrElse(sb, format, i + 1, end, args, maxArgKeyLength, value);
                    last = i + 1;
                }
            }
            else if(c == '%'){
                int o = i + 1;
                if(o == end) continue;
                char curChar = 0;

                int oi = o;
                for(;oi<end;oi++){
                    curChar = format.charAt(oi);
                    if(!Character.isDigit(curChar)) break;
                    if(oi + 1 == end) continue outerLoop;
                }

                String _alignNum = format.substring(o, oi);
                int alignNum = Integer.parseInt(_alignNum);

                o = oi;

                if(curChar != '<' && curChar != '|' && curChar != '>') continue;
                char alignSign = curChar;
                o++;
                if(o == end) continue;
                curChar = format.charAt(o);

                String alignFill = " ";

                if(curChar == '[') {
                    o++;
                    if(o == end) continue;
                    int p = o;
                    for(;p<end;p++){
                        curChar = format.charAt(p);
                        if(curChar == ']') break;
                        if(p + 1 == end) continue outerLoop;
                    }

                    alignFill = format.substring(o, p);
                    o = p + 1;
                    if(o == end) continue;
                    curChar = format.charAt(o);
                }

                if(curChar == '('){
                    o++;
                    if(o == end) continue;
                    int p = o;
                    int open = 1;
                    for(;p<end;p++){
                        curChar = format.charAt(p);
                        if(curChar == '(') {
                            open++;
                        }
                        else if(curChar == ')'){
                            open--;
                            if(open == 0) break;
                        }
                        else if(p + 1 == end) continue outerLoop;
                    }

                    String content = format(format, o, p, args, maxArgKeyLength);

                    sb.append(format, last, i);
                    if(alignSign == '<'){
                        alignLeft(sb, content, alignFill, alignNum);
                    }
                    else if(alignSign == '|'){
                        alignCenter(sb, content, alignFill, alignNum);
                    }
                    else {
                        alignRight(sb, content, alignFill, alignNum);
                    }

                    i = p;
                    last = i + 1;
                }
            }
        }

        if(last < end) {
            sb.append(format, last, end);
        }

        return sb.toString();
    }

    private static int appendValueOrElse(StringBuilder sb, String format, int i, int end, Map<String, Object> args, int maxArgKeyLength, Object value){
        int o = i;
        if(o != end && format.charAt(o) == '?'){
            if(++o != end && format.charAt(o) == '{'){
                if(++o != end){
                    int p = o + 1;
                    if(p != end){
                        int open = 1;
                        for(;p<end;p++){
                            char c = format.charAt(p);
                            if(c == '{'){
                                open++;
                            }
                            else if(c == '}'){
                                open--;
                                if(open == 0) break;
                            }
                            else if(p + 1 == end) {
                                sb.append(value);
                                return i;
                            }
                        }

                        if(value == null) {
                            String content = format(format, o, p, args, maxArgKeyLength);
                            sb.append(content);
                        }
                        else {
                            sb.append(value);
                        }

                        return p;
                    }
                }
            }
        }

        sb.append(value);
        return i - 1;
    }

    private static void alignLeft(StringBuilder sb, String content, String replace, int size){
        sb.append(content);
        size -= lengthOfContent(content);

        int len = replace.length();

        for(int i=0;i<size;i++){
            int ri = i % len;
            sb.append(replace.charAt(ri));
        }
    }

    private static void alignCenter(StringBuilder sb, String content, String replace, int size){
        size -= lengthOfContent(content);

        int s1 = size / 2;
        int s2 = s1 + size % 2;
        int len = replace.length();

        for(int i=0;i<s1;i++){
            int ri = i % len;
            sb.append(replace.charAt(ri));
        }

        sb.append(content);

        for(int i=0;i<s2;i++){
            int ri = i % len;
            sb.append(replace.charAt(ri));
        }
    }

    private static void alignRight(StringBuilder sb, String content, String replace, int size){
        size -= lengthOfContent(content);

        int len = replace.length();

        for(int i=0;i<size;i++){
            int ri = i % len;
            sb.append(replace.charAt(ri));
        }

        sb.append(content);
    }

    private static int lengthOfContent(String content){
        String resultString = content.replaceAll("\u001B\\[[0-9]+m", "");
        return resultString.length();
    }

    private static String getDate(){
        return "";
    }

    private static String getTime(){
        return "";
    }

}
