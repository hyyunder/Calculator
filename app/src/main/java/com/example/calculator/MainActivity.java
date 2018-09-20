package com.example.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.widget.*;
import android.view.View;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.math.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Text();
        Button btnRes=(Button)findViewById(R.id.buttonRes);
        final TextView textRes=(TextView)findViewById(R.id.textRes);
        final TextView text=(TextView)findViewById(R.id.text);

        btnRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = text.getText().toString();
                //将字符串用运算符分割成一个个的数字字符串
                String[] textArray=txt.split("＋|-|×|÷|\\(|\\)|²|%|\\/|sin|cos|tan|³|log|!|e");
                //将数组转换为list集合
                List<String> list1= Arrays.asList(textArray);
                //转换为ArrayLsit调用相关的remove方法
                List<String> arrayNum = new ArrayList<String>(list1);
                //记录数字和符号的栈
                Stack<Double> number = new Stack<Double>();
                Stack<Character> symbol = new Stack<Character>();
                //将字符串中的运算符提取出来并转换为list数组
                for(int i=0;i<arrayNum.size();i++){
                    System.out.println("Before:arrayNum["+i+"]="+arrayNum.get(i));
                }
                for(int i=arrayNum.size()-1;i>=0;i--){
                    if(arrayNum.get(i).isEmpty()==true){
                        System.out.println("-------");
                        arrayNum.remove(i);
                    }
                }
                List<Character> arraySymbol = new ArrayList<Character>();
                for(int i=0;i<txt.length();i++){
                    if(Character.isDigit(txt.charAt(i))==false&&txt.charAt(i)!='.') {
                        arraySymbol.add(txt.charAt(i));
                    }
                }
                for(int i=0;i<arrayNum.size();i++){
                    System.out.println("arrayNum["+i+"]="+arrayNum.get(i));
                }
                for(int i=0;i<arraySymbol.size();i++){
                    System.out.println("arraySymbol["+i+"]="+arraySymbol.get(i));
                }
                calSpecial(arrayNum,arraySymbol);
                if(arraySymbol.contains('(')){
                    calBracket(arrayNum,arraySymbol,number,symbol);
                }
                double res;
                if(arrayNum.size()==1){
                    res=Double.parseDouble(arrayNum.get(0));
                }else {
                    res = calucator(arrayNum, arraySymbol, number, symbol, 0, arrayNum.size());
                }
                final String result = Double.toString(res);
                textRes.setText("==" + result);
            }
        });

    }

    public double cal(double num1,double num2,char sym){
        if(sym=='＋'){
            return num2+num1;
        }else if(sym=='-'){
            return num2-num1;
        }else if(sym=='×'){
            return num1*num2;
        }else if(sym=='÷'){
            return num2/num1;
        }
        return 0;
    }

    public void calSpecial(List<String> arrayNum,List<Character> arraySymbol){
        for(int i=0;i<arraySymbol.size();i++){
            if(arraySymbol.get(i)=='³'){
                double x=Double.parseDouble(arrayNum.get(i));
                double x2=x*x*x;
                arrayNum.set(i,Double.toString(x2));
                arraySymbol.remove(i);
            }else if(arraySymbol.get(i)=='²'){
                double x=Double.parseDouble(arrayNum.get(i));
                double x2=x*x;
                arrayNum.set(i,Double.toString(x2));
                arraySymbol.remove(i);
            }else if(arraySymbol.get(i)=='%'){
                double x=Double.parseDouble(arrayNum.get(i));
                double x2=x*0.01;
                arrayNum.set(i,Double.toString(x2));
                arraySymbol.remove(i);
            }else if(arraySymbol.get(i)=='/'){
                double x=Double.parseDouble(arrayNum.get(i));
                double x2=1/x;
                arrayNum.set(i,Double.toString(x2));
                arraySymbol.remove(i);
            }else if(arraySymbol.get(i)=='s'){
                double x=Double.parseDouble(arrayNum.get(i));
                double x2=Math.sin(x);
                arrayNum.set(i,Double.toString(x2));
                arraySymbol.remove(i+2);
                arraySymbol.remove(i+1);
                arraySymbol.remove(i);
            }else if(arraySymbol.get(i)=='c') {
                double x = Double.parseDouble(arrayNum.get(i));
                double x2 = Math.cos(x);
                arrayNum.set(i, Double.toString(x2));
                arraySymbol.remove(i + 2);
                arraySymbol.remove(i + 1);
                arraySymbol.remove(i);
            }else if(arraySymbol.get(i)=='t'){
                double x = Double.parseDouble(arrayNum.get(i));
                double x2 = Math.tan(x);
                arrayNum.set(i, Double.toString(x2));
                arraySymbol.remove(i + 2);
                arraySymbol.remove(i + 1);
                arraySymbol.remove(i);
            }else if(arraySymbol.get(i)=='l'&&arraySymbol.get(i+1)=='o'){
                double x = Double.parseDouble(arrayNum.get(i));
                double x2 = Math.log(x);
                arrayNum.set(i, Double.toString(x2));
                arraySymbol.remove(i + 2);
                arraySymbol.remove(i + 1);
                arraySymbol.remove(i);
            }else if(arraySymbol.get(i)=='!'){
                int x = Integer.parseInt(arrayNum.get(i));
                double re=1;
                for(int j=2;j<=x;j++){
                    re=re*j;
                }
                arrayNum.set(i, Double.toString(re));
                arraySymbol.remove(i);
            }else if(arraySymbol.get(i)=='e'){
                double x2=Math.E;
                arrayNum.add(i,Double.toString(x2));
                arraySymbol.remove(i);
            }
        }
    }
    public void calBracket(List<String> arrayNum,List<Character> arraySymbol,Stack<Double> number,Stack<Character> symbol){
        int position1 = arraySymbol.indexOf('(');
        int position2 = arraySymbol.lastIndexOf(')');
        System.out.println("position1="+position1);
        System.out.println("position2="+position2);
        arraySymbol.remove(position2);
        arraySymbol.remove(position1);
        double res=calucator(arrayNum,arraySymbol,number,symbol,position1,position2);
//        for(int i=0;i<arraySymbol.size();i++){
//            System.out.println("before:arraySymbol["+i+"]="+arraySymbol.get(i));
//        }
        System.out.println("position2-1="+(position2-1));
        for(int i=position2-2;i>=position1;i--){
            arraySymbol.remove(i);
        }
//        for(int i=0;i<arraySymbol.size();i++){
//            System.out.println("after:arraySymbol[i]="+arraySymbol.get(i));
//        }
//        for(int i=0;i<arrayNum.size();i++){
//            System.out.println("before:arrayNum["+i+"]="+arrayNum.get(i));
//        }
        arrayNum.set(position1,Double.toString(res));
        for(int i=position2-1;i>=position1+1;i--){
            arrayNum.remove(i);
        }
//        for(int i=0;i<arrayNum.size();i++){
//            System.out.println("after:arrayNum[i]="+arrayNum.get(i));
//        }
    }

    public double calucator(List<String> arrayNum,List<Character> arraySymbol,Stack<Double> number,Stack<Character> symbol,int pos,int las){
        System.out.println("pos="+pos);
        System.out.println("las="+las);
        System.out.println("arrayNum.get(pos)="+arrayNum.get(pos));
        double d0 = Double.parseDouble(arrayNum.get(pos));
        double d1 = Double.parseDouble(arrayNum.get(pos+1));
        number.push(d0);
        number.push(d1);
        char d2 = arraySymbol.get(pos);
        symbol.push(d2);
        System.out.println("d0="+d0);
        System.out.println("d1="+d1);
        System.out.println("d2="+d2);
        for(int i=pos+2;i<las;i++){
            double numberI = Double.parseDouble(arrayNum.get(i));
            char symbolI = arraySymbol.get(i-1);
            char c2=symbol.pop();
            System.out.println("numberI="+numberI);
            System.out.println("symbolI="+symbolI);
            if(c2=='＋'&&symbolI=='×'){
                double c4 = number.pop();
                double res=c4*numberI;
                number.push(res);
                symbol.push(c2);
                System.out.println("res="+res);
                System.out.println("c2="+c2);
                Log.e("mylog","11111");
            }else if(c2=='＋'&&symbolI=='÷'){
                double c4 = number.pop();
                double res=c4/numberI;
                number.push(res);
                symbol.push(c2);
            }else if(c2=='-'&&symbolI=='×'){
                double c4 = number.pop();
                double res=c4*numberI;
                number.push(res);
                symbol.push(c2);
            }else if(c2=='-'&&symbolI=='÷'){
                double c4 = number.pop();
                double res=c4/numberI;
                number.push(res);
                symbol.push(c2);
            }else if(c2=='＋'){
                double numberPop1 = number.pop();
                double numberPop2 = number.pop();
                double res=numberPop1+numberPop2;
                number.push(res);
                symbol.push(symbolI);
                number.push(numberI);
            }else if(c2=='-'){
                double numberPop1 = number.pop();
                double numberPop2 = number.pop();
                double res=numberPop2-numberPop1;
                number.push(res);
                symbol.push(symbolI);
                number.push(numberI);
            }else if(c2=='×'){
                double numberPop1 = number.pop();
                double numberPop2 = number.pop();
                double res=numberPop1*numberPop2;
                number.push(res);
                symbol.push(symbolI);
                number.push(numberI);
            }else if(c2=='÷'){
                double numberPop1 = number.pop();
                double numberPop2 = number.pop();
                double res=numberPop2/numberPop1;
                number.push(res);
                symbol.push(symbolI);
                number.push(numberI);
            }
        }
        double resBe1 = number.pop();
        double resBe2 = number.pop();
        char resBe3 = symbol.pop();
        double res = cal(resBe1,resBe2,resBe3);
        System.out.println("res="+res);
        return res;
    }

    //点击按钮后显示在TextView上
    public void Text(){
        final TextView txtResult=(TextView)findViewById(R.id.text);
        final TextView txtR=(TextView)findViewById(R.id.textRes);
        Button btnOne=(Button)findViewById(R.id.button1);
        Button btn2=(Button)findViewById(R.id.button2);
        Button btn3=(Button)findViewById(R.id.button3);
        Button btn4=(Button)findViewById(R.id.button4);
        Button btn5=(Button)findViewById(R.id.button5);
        Button btn6=(Button)findViewById(R.id.button6);
        Button btn7=(Button)findViewById(R.id.button7);
        Button btn8=(Button)findViewById(R.id.button8);
        Button btn9=(Button)findViewById(R.id.button9);
        Button btn0=(Button)findViewById(R.id.button0);
        Button btnPoint=(Button)findViewById(R.id.buttonPoint);
        Button btnAdd=(Button)findViewById(R.id.buttonAdd);
        Button btnSub=(Button)findViewById(R.id.buttonSub);
        Button btnMul=(Button)findViewById(R.id.buttonMul);
        Button btnDiv=(Button)findViewById(R.id.buttonDiv);
        Button btnBack=(Button)findViewById(R.id.buttonBack);
        Button btnClear=(Button)findViewById(R.id.buttonClear);
        Button btnLeft=(Button)findViewById(R.id.buttonLeft);
        Button btnRight=(Button)findViewById(R.id.buttonRight);
        Button btnSin=(Button)findViewById(R.id.buttonSin);
        Button btnCos=(Button)findViewById(R.id.buttonCos);
        Button btnBai=(Button)findViewById(R.id.buttonBai);
        Button btnPin=(Button)findViewById(R.id.buttonPin);
        Button btnFen=(Button)findViewById(R.id.buttonFen);
        Button btnTan=(Button)findViewById(R.id.buttonTan);
        Button btnLog=(Button)findViewById(R.id.buttonLog);
        Button btnX=(Button)findViewById(R.id.buttonX);
        Button btnLi=(Button)findViewById(R.id.buttonLi);
        Button btnE=(Button)findViewById(R.id.buttonE);

        btnE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "e");
            }
        });

        btnLi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "³");
            }
        });

        btnX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "!");
            }
        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "log");
            }
        });

        btnTan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "tan");
            }
        });

        btnSin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "sin");
            }
        });

        btnCos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "cos");
            }
        });

        btnBai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "%");
            }
        });

        btnPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "²");
            }
        });

        btnFen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "/");
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "(");
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + ")");
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText("");
                txtR.setText("==");
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = txtResult.getText().toString();
                a = a.substring(0,a.length() - 1);
                txtResult.setText(a);
            }
        });

        btnPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + ".");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "＋");
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "-");
            }
        });

        btnMul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "×");
            }
        });

        btnDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "÷");
            }
        });

        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "1");
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "2");
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "3");
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "4");
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "5");
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "6");
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "7");
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "8");
            }
        });

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "9");
            }
        });

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtResult.setText(txtResult.getText() + "0");
            }
        });
    }

    //简单运算（只有加减乘除）
//    public static void easy(){
//        String txt = text.getText().toString();
//        String[] textArray=txt.split("＋|-|×|÷");//将字符串用运算符分割成一个个的数字字符串
//        List<String> list= Arrays.asList(textArray);//将数组转换为list集合
//        List<String> arrayList = new ArrayList<String>(list);//转换为ArrayLsit调用相关的remove方法
//        char[] arr = txt.toCharArray();//将字符串转为字符数组
//
//        for(int i=0;i<arr.length;i++){
//            if(arr[i]=='＋'){
//                double a = Double.parseDouble(arrayList.get(0));
//                double b = Double.parseDouble(arrayList.get(1));
//                double c=a+b;
//                String s=Double.toString(c);
//                arrayList.set(0,s);
//                arrayList.remove(arrayList.get(1));
//            }else if(arr[i]=='-'){
//                double a = Double.parseDouble(arrayList.get(0));
//                double b = Double.parseDouble(arrayList.get(1));
//                double c=a-b;
//                String s=Double.toString(c);
//                arrayList.set(0,s);
//                arrayList.remove(arrayList.get(1));
//            }else if(arr[i]=='×') {
//                double a = Double.parseDouble(arrayList.get(0));
//                double b = Double.parseDouble(arrayList.get(1));
//                double c = a * b;
//                System.out.println(i+":"+arrayList.get(0)+"  "+arrayList.get(1));
//                String s = Double.toString(c);
//                arrayList.set(0, s);
//                arrayList.remove(arrayList.get(1));
//                Log.e("mylog","11111");
//                System.out.println(i+":"+arrayList.get(0));
//            }else if(arr[i]=='÷'){
//                double a = Double.parseDouble(arrayList.get(0));
//                double b = Double.parseDouble(arrayList.get(1));
//                double c=a/b;
//                String s=Double.toString(c);
//                arrayList.set(0,s);
//                arrayList.remove(arrayList.get(1));
//            }
//        }
//        final String result=arrayList.get(0);
//        textRes.setText("=="+result);
//    }

}
