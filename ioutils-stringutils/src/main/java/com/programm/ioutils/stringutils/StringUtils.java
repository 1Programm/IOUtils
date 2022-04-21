package com.programm.ioutils.stringutils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static int findClosing(String text, String open, String closing, String... commentsOpenClose){
        return findClosing(text, 0, open, closing, commentsOpenClose);
    }

    public static int findClosing(String text, int offset, String open, String closing, String... commentsOpenClose){
        if(commentsOpenClose.length % 2 != 0) throw new IllegalArgumentException("Comments must be specified in pairs!");

        int numOpen = 1;
        int index = offset;
        boolean insideComment = false;
        String commentClose = null;

        while(index < text.length()){
            char c = text.charAt(index);

            if(insideComment){
                if(c == commentClose.charAt(0) && text.startsWith(commentClose, index)){
                    insideComment = false;
                    commentClose = null;
                }
            }
            else if(c == open.charAt(0) && text.startsWith(open, index)){
                numOpen++;
            }
            else if(c == closing.charAt(0) && text.startsWith(closing, index)){
                numOpen--;
                if(numOpen == 0){
                    return index;
                }
            }
            else {
                for(int i=0;i<commentsOpenClose.length;i+=2){
                    String cOpen = commentsOpenClose[i];
                    if(c == cOpen.charAt(0) && text.startsWith(cOpen, index)){
                        insideComment = true;
                        commentClose = commentsOpenClose[i+1];
                        break;
                    }
                }
            }

            index++;
        }

        return -1;
    }

    public static String[] advancedSplit(String text, String key, String... comments){
        if(comments.length % 2 != 0) throw new IllegalArgumentException("Comments must be specified in pairs!");

        List<String> split = new ArrayList<>();

        int last = 0;
        int index = 0;
        boolean insideComment = false;
        String commentClose = null;

        while(index < text.length()){
            char c = text.charAt(index);

            if(insideComment){
                if(c == commentClose.charAt(0) && text.startsWith(commentClose, index)){
                    insideComment = false;
                    commentClose = null;
                }
            }
            else if(c == key.charAt(0) && text.startsWith(key, index)){
                String nSplit = text.substring(last, index);
                split.add(nSplit);
                last = index + key.length();
            }
            else {
                for(int i=0;i<comments.length;i+=2){
                    String cOpen = comments[i];
                    if(c == cOpen.charAt(0) && text.startsWith(cOpen, index)){
                        insideComment = true;
                        commentClose = comments[i+1];
                        break;
                    }
                }
            }

            index++;
        }

        if(last < text.length()){
            String rest = text.substring(last);
            split.add(rest);
        }

        return split.toArray(new String[0]);
    }

    public static int next(String s, String... searches){
        return next(s, 0, s.length(), searches);
    }

    public static int next(String s, int start, String... searches){
        return next(s, start, s.length(), searches);
    }

    public static int next(String s, int start, int end, String... searches){
        if(searches == null || searches.length == 0) return -1;
        if(searches.length == 1) {
            String toSearch = searches[0];
            if(toSearch.isEmpty()) return -1;
            return s.indexOf(toSearch);
        }

        boolean[] ignore = new boolean[searches.length];

        int sLen = s.length();

        boolean hasAtLeastOne = false;
        for(int i=0;i<searches.length;i++){
            if(searches[i].isEmpty()){
                ignore[i] = true;
            }
            else {
                ignore[i] = false;
                hasAtLeastOne = true;
            }
        }
        if(!hasAtLeastOne) return -1;

        for(int i=start;i<end;i++){
            char c = s.charAt(i);

            for(int o=0;o<searches.length;o++){
                String search = searches[o];

                if(!ignore[o]){
                    int len = search.length();
                    if(search.charAt(0) == c && i + len <= sLen){
                        boolean brokeOut = false;

                        for(int l=1;l<len;l++){
                            if(search.charAt(l) != s.charAt(i + l)){
                                brokeOut = true;
                                break;
                            }
                        }

                        if(!brokeOut){
                            return i;
                        }
                    }
                }
            }
        }

        return -1;
    }

    public interface SearchVisitor {
        boolean visit(String search, int index);
    }

    public static void visitSearch(String s, SearchVisitor visitor, String... searches){
        visitSearch(s, 0, s.length(), visitor, searches);
    }

    public static void visitSearch(String s, int start, SearchVisitor visitor, String... searches){
        visitSearch(s, start, s.length(), visitor, searches);
    }

    public static void visitSearch(String s, int start, int end, SearchVisitor visitor, String... searches){
        if(searches == null || searches.length == 0) return;
        int sLen = s.length();

        for(int i=start;i<end;i++){
            char c = s.charAt(i);

            for (String search : searches) {
                int len = search.length();
                if (search.charAt(0) == c && i + len <= sLen) {
                    boolean brokeOut = false;

                    for (int l = 1; l < len; l++) {
                        if (search.charAt(l) != s.charAt(i + l)) {
                            brokeOut = true;
                            break;
                        }
                    }

                    if (!brokeOut) {
                        boolean shouldExit = visitor.visit(search, i);
                        if (shouldExit) return;
                    }
                }
            }
        }
    }
}
