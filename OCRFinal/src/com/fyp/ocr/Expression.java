package com.fyp.ocr;


import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

import android.util.Log;





public class Expression {
	 private static final String TAG = "SimpleAndroidOCR.java";
	static String expr;
 


 static List<String> getInFix(String expr) {
     List<String> list = new ArrayList<String>();
     int n, i;
     char ch;
     boolean hasInt;

     for(i = n = 0, hasInt = false; i < expr.length(); i++) {
         ch = expr.charAt(i);
         if(!isDigit(ch)) {
             if(hasInt) {
                 list.add("" + n);
                 n = 0;
                 hasInt = false;
             }
             list.add("" + ch);
         }
         else {
             n = n * 10 + (ch - 48);
             hasInt = true;
         }
     }
     
     return list;
 }

 
 static List<String> getPostFix(List<String> inFix) {
     List<String> list = new ArrayList<String>();
     Stack<String> oper = new Stack<String>();
     int i;
     char ch;
     String token, peek;

     for(i = 0; i < inFix.size(); i++) {
         token = inFix.get(i);
         ch = token.charAt(0);
         if(isDigit(ch)) list.add(token);
         else if(ch=='(') oper.push("" + ch);
         else if(ch==')') {
             while(!oper.empty()) {
                 peek = oper.pop();
                 if(peek.charAt(0)!='(') list.add(peek);
                 else break;
             }
         }
         else {
             while(!oper.empty()) {
                 peek = oper.peek();
                 if(peek.charAt(0)!='(' && preced(ch) <= 

preced(peek.charAt(0))) {
                     list.add(peek);
                     oper.pop();
                 }
                 else {
                     oper.push(token);
                     break;
                 }
             }
         }
     }
     
     return list;
 }

 
 static int evaluate(List<String> postFix) {
     Stack<Integer> stack = new Stack<Integer>();
     int i, a, b;
     String token;
     char ch;

     for(i = 0; i < postFix.size(); i++) {
         token = postFix.get(i);
         ch = token.charAt(0);
         if(isDigit(ch)) stack.push(Integer.parseInt(token));
         else {
             b = stack.pop();
             a = stack.pop();
             switch(ch) {
                 case '+': a = a + b; break;
                 case '-': a = a - b; break;
                 case '*': a = a * b; break;
                 case '/': a = a / b; break;
                 case '%': a = a % b; break;
                 case '^': a = (int)Math.pow(a, b); break;
             }
             stack.push(a);
         }
     }
     
     return stack.pop();
 }

 
 static int preced(char op) {
     if(op=='"') return 3;
     if(op=='*' || op=='/' || op=='%') return 2;
     if(op=='+' || op=='-') return 1;
     return 0;
 }

 
 static boolean isDigit(char ch) {
     return (ch >= '0' && ch <= '9');
 }

public int calc(String recognizedText) {
	
	int result = 0;
	 List<String> inFix, postFix;
	   expr=recognizedText;
	   expr="("+expr+")";
		inFix = getInFix(expr);
		Log.v(TAG, "Ot1: " + inFix );
        postFix = getPostFix(inFix);
        Log.v(TAG, "Ot2: " + postFix );
        result = evaluate(postFix);
       
	   return result;
}

}
