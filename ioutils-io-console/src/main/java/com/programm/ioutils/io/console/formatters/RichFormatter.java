package com.programm.ioutils.io.console.formatters;

import com.programm.ioutils.io.api.IFormatter;
import com.programm.ioutils.stringutils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RichFormatter implements IFormatter {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    private static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private static final String ANSI_CMD_CLEAR = "\033[H\033[2J";
    private static final String ANSI_CMD_BACK = "\r";

    private final String key;
    private final Map<String, Function<String, String>> formatter = new HashMap<>();
    private final Map<String, BiFunction<String, String, String>> surroundFormatter = new HashMap<>();

    public RichFormatter(String key) {
        this.key = key;

        registerColor("black", ANSI_BLACK);
        registerColor("red", ANSI_RED);
        registerColor("green", ANSI_GREEN);
        registerColor("yellow", ANSI_YELLOW);
        registerColor("blue", ANSI_BLUE);
        registerColor("purple", ANSI_PURPLE);
        registerColor("cyan", ANSI_CYAN);
        registerColor("white", ANSI_WHITE);

        registerColor("bg_black", ANSI_BLACK_BACKGROUND);
        registerColor("bg_red", ANSI_RED_BACKGROUND);
        registerColor("bg_green", ANSI_GREEN_BACKGROUND);
        registerColor("bg_yellow", ANSI_YELLOW_BACKGROUND);
        registerColor("bg_blue", ANSI_BLUE_BACKGROUND);
        registerColor("bg_purple", ANSI_PURPLE_BACKGROUND);
        registerColor("bg_cyan", ANSI_CYAN_BACKGROUND);
        registerColor("bg_white", ANSI_WHITE_BACKGROUND);


        formatter.put("clear", w -> ANSI_CMD_CLEAR);
        formatter.put("back", w -> ANSI_CMD_BACK);

        //w -> for:1-10
        surroundFormatter.put("for", (w, t) -> {
            int splitIndex = w.indexOf(':');

            int start = 0;
            int end = 0;

            if(splitIndex != -1) {
                String rest = w.substring(splitIndex + 1);

                //for:1-10
                String[] split = rest.split("-");
                String _start = split[0];

                try {
                    start = Integer.parseInt(_start);
                }
                catch (NumberFormatException ignored){}

                if(split.length == 1){
                    end = start;
                    start = 0;
                }
                else {
                    String _end = split[1];
                    try {
                        end = Integer.parseInt(_end);
                        end++;
                    }
                    catch (NumberFormatException ignored){}
                }
            }

            StringBuilder sb = new StringBuilder();

            for(int i=start;i<end;i++){
                String nt = t.replaceAll("\\$i", "" + i);
                sb.append(nt);
            }

            return sb.toString();
        });
    }

    public RichFormatter registerFormatter(String key, Function<String, String> formatter){
        this.formatter.put(key, formatter);
        return this;
    }

    public RichFormatter registerSurroundingFormatter(String key, BiFunction<String, String, String> formatter){
        this.surroundFormatter.put(key, formatter);
        return this;
    }

    private void registerColor(String name, String ansiColor){
        formatter.put(name, w -> ansiColor);
        surroundFormatter.put(name, (w, t) -> ansiColor + t + ANSI_RESET);
    }

    @Override
    public String format(String message, Object... args) {
        int last = 0;
        int index = message.indexOf(key);

        if(index == -1) return message;

        StringBuilder sb = new StringBuilder();

        while(index != -1){
            sb.append(message, last, index);

            int bracketOpen = index + key.length();
            if(message.charAt(bracketOpen) != '{'){
                sb.append(key);
                last = bracketOpen;
                index = message.indexOf(key, last);
                continue;
            }

            int bracketClose = message.indexOf('}', bracketOpen);
            if(bracketClose == -1){
                sb.append(message, index, message.length());
                last = message.length();
                break;
            }

            String word = message.substring(bracketOpen + 1, bracketClose);
            boolean surrounds = false;
            String surroundedText = null;

            int rBracketOpen = bracketClose + 1;
            int rBracketClose = StringUtils.findClosing(message, rBracketOpen + 1, "(", ")");

            if(rBracketOpen < message.length() && message.charAt(rBracketOpen) == '('){
                if(rBracketClose != -1){
                    surrounds = true;
                    surroundedText = message.substring(rBracketOpen + 1, rBracketClose);
                }
            }

            String firstWord = word.split(":")[0];

            if(!surrounds){
                Function<String, String> f = formatter.get(firstWord);
                if(f != null) {
                    String nText = f.apply(word);
                    sb.append(nText);
                    last = bracketClose + 1;
                }
                else {
                    sb.append(key);
                    last = bracketOpen;
                }
            }
            else {
                BiFunction<String, String, String> f = surroundFormatter.get(firstWord);
                if(f != null) {
                    surroundedText = format(surroundedText);

                    String nText = f.apply(word, surroundedText);
                    sb.append(nText);
                    last = rBracketClose + 1;
                }
                else {
                    sb.append(key);
                    last = bracketOpen;
                }
            }

            index = message.indexOf(key, last);
        }

        if(last <= message.length()){
            sb.append(message, last, message.length());
        }

        return sb.toString();
    }

}
