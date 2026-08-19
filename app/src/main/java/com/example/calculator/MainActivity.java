package com.example.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.Stack;

public class MainActivity extends Activity {
    TextView display; String expr="";
    @Override public void onCreate(Bundle b){super.onCreate(b); setContentView(R.layout.activity_main); display=findViewById(R.id.display);}
    public void buttonClick(View v){
        String s=((android.widget.Button)v).getText().toString();
        if(s.equals("C")) expr="";
        else if(s.equals("⌫")) { if(expr.length()>0) expr=expr.substring(0,expr.length()-1); }
        else if(s.equals("=")) { try { expr=fmt(eval(expr)); } catch(Exception e){ expr="خطأ"; } }
        else { if(expr.equals("خطأ")) expr=""; expr+=s; }
        display.setText(expr.length()==0?"0":expr);
    }
    String fmt(double x){ if(x==Math.rint(x)) return String.valueOf((long)x); return String.valueOf(x); }
    double eval(String in){
        in=in.replace("×","*").replace("÷","/").replace("−","-");
        Stack<Double> nums=new Stack<>(); Stack<Character> ops=new Stack<>();
        for(int i=0;i<in.length();){
            char c=in.charAt(i);
            if(c==' '){i++;continue;}
            if(Character.isDigit(c)||c=='.'||(c=='-'&&(i==0||in.charAt(i-1)=='('))){
                int j=i+1; while(j<in.length()&&(Character.isDigit(in.charAt(j))||in.charAt(j)=='.'))j++;
                nums.push(Double.parseDouble(in.substring(i,j))); i=j; continue;
            }
            if(c=='('){ops.push(c);i++;continue;}
            if(c==')'){while(!ops.empty()&&ops.peek()!='(')apply(nums,ops.pop()); if(ops.empty())throw new RuntimeException();ops.pop();i++;continue;}
            if("+-*/".indexOf(c)>=0){while(!ops.empty()&&ops.peek()!='('&&prec(ops.peek())>=prec(c))apply(nums,ops.pop());ops.push(c);i++;continue;}
            throw new RuntimeException();
        }
        while(!ops.empty())apply(nums,ops.pop()); if(nums.size()!=1)throw new RuntimeException(); return nums.pop();
    }
    int prec(char c){return c=='+'||c=='-'?1:2;}
    void apply(Stack<Double> n,char o){double b=n.pop(),a=n.pop(); if(o=='+')n.push(a+b);else if(o=='-')n.push(a-b);else if(o=='*')n.push(a*b);else {if(b==0)throw new ArithmeticException();n.push(a/b);}}
}
