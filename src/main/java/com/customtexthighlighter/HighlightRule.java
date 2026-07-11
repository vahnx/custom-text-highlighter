package com.customtexthighlighter;

import java.awt.Color;
import java.util.regex.*;
import net.runelite.client.util.ColorUtil;

final class HighlightRule{
 private final String text; private final Pattern pattern; private final Color colour;
 private HighlightRule(String t,Pattern p,Color c){text=t;pattern=p;colour=c;}
 static HighlightRule parse(String ruleText){
  String rule=ruleText.trim();
  String c=rule.substring(1,rule.length()-1);
  int i=c.lastIndexOf(',');
  String text=c.substring(0,i).trim();
  Color colour=ColorParser.parse(c.substring(i+1).trim());
  return new HighlightRule(text,Pattern.compile(buildPattern(text),
   Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE|Pattern.UNICODE_CHARACTER_CLASS),colour);
 }
 private static String buildPattern(String text){
  StringBuilder r=new StringBuilder(),lit=new StringBuilder();
  for(char ch:text.toCharArray()){
   switch(ch){
    case '*': q(r,lit); r.append("[\\p{L}\\p{N}_']*"); break;
    case '?': q(r,lit); r.append('.'); break;
    case '|': q(r,lit); r.append('|'); break;
    default: lit.append(ch);
   }
  }
  q(r,lit); return r.toString();
 }
 private static void q(StringBuilder r,StringBuilder l){
  if(l.length()>0){r.append(Pattern.quote(l.toString())); l.setLength(0);}
 }
 String getText(){return text;}
 String apply(String m){
  Matcher matcher=pattern.matcher(m);
  if(!matcher.find()) return null;
  matcher.reset();
  StringBuffer sb=new StringBuffer();
  while(matcher.find()){
   matcher.appendReplacement(sb,Matcher.quoteReplacement(ColorUtil.wrapWithColorTag(matcher.group(),colour)));
  }
  matcher.appendTail(sb);
  return sb.toString();
 }
}
