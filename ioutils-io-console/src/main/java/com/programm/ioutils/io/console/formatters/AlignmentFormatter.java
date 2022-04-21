package com.programm.ioutils.io.console.formatters;


import com.programm.ioutils.io.api.IFormatter;
import com.programm.ioutils.stringutils.StringUtils;

/**
 * # Example 1:
 * | key: %
 * | Input:  'Test %10<(Text) Bla'
 * | Output: 'Test Text      Bla'
 *
 * # Example 2:
 * | key: #
 * | Input:  '#20|[*]( :D )'
 * | Output: '******** :D ********'
 *
 * # Example 3:
 * | key: %
 * | Input:  '#20>[- ](My Test Title)'
 * | Output: '- - - -My Test Title'
 *
 */
public class AlignmentFormatter implements IFormatter {

    public static final char ALIGN_LEFT   = '<';
    public static final char ALIGN_CENTER = '|';
    public static final char ALIGN_RIGHT  = '>';

    private final String key;

    public AlignmentFormatter(String key) {
        this.key = key;
    }

    @Override
    public String format(String message, Object... args) {
        int index = message.indexOf(key);
        int last = 0;

        if(index == -1) return message;

        StringBuilder sb = new StringBuilder();

        while(index != -1){
            sb.append(message, last, index);

            int rBracketOpen = message.indexOf('(', index);

            if(rBracketOpen == -1) {
                last = index;
                break;
            }

            int rBracketClose = StringUtils.findClosing(message, rBracketOpen + 1, "(", ")");

            if(rBracketClose == -1) {
                last = index;
                break;
            }

            String content = message.substring(rBracketOpen + 1, rBracketClose);
            String replace = " ";
            int indexOperator = rBracketOpen - 1;
            boolean err = false;

            if(message.charAt(rBracketOpen - 1) == ']'){
                int sBracketOpen = message.indexOf('[', index);

                if(sBracketOpen == -1){
                    err = true;
                }
                else {
                    replace = message.substring(sBracketOpen + 1, rBracketOpen - 1);
                    indexOperator = sBracketOpen - 1;
                }
            }

            if(!err){
                char operator = message.charAt(indexOperator);
                String _num = message.substring(index + 1, indexOperator);
                int num = 0;

                try {
                    num = Integer.parseInt(_num);
                }
                catch (NumberFormatException e){
                    err = true;
                }

                if(!err) {
                    if (operator == ALIGN_LEFT) {
                        alignLeft(sb, content, replace, num);
                    }
                    else if (operator == ALIGN_CENTER) {
                        alignCenter(sb, content, replace, num);
                    }
                    else if (operator == ALIGN_RIGHT) {
                        alignRight(sb, content, replace, num);
                    }
                }
            }

            if(err) {
                sb.append(key);
                last = index + 1;
            }
            else {
                last = rBracketClose + 1;
            }

            index = message.indexOf(key, last);
        }

        sb.append(message, last, message.length());

        return sb.toString();
    }

    private void alignLeft(StringBuilder sb, String content, String replace, int size){
        sb.append(content);
        size -= lengthOfContent(content);

        int len = replace.length();

        for(int i=0;i<size;i++){
            int ri = i % len;
            sb.append(replace.charAt(ri));
        }
    }

    private void alignCenter(StringBuilder sb, String content, String replace, int size){
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

    private void alignRight(StringBuilder sb, String content, String replace, int size){
        size -= lengthOfContent(content);

        int len = replace.length();

        for(int i=0;i<size;i++){
            int ri = i % len;
            sb.append(replace.charAt(ri));
        }

        sb.append(content);
    }

    private int lengthOfContent(String content){
        String resultString = content.replaceAll("\u001B\\[[0-9]+m", "");
        return resultString.length();
    }
}
