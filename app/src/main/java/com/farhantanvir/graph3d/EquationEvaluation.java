package com.farhantanvir.graph3d;

public class EquationEvaluation{

    String equation;
    String parse[];
    int numOfTerms;
    final int CONSTANT = 1;
    final int VARIBLE = 2;
    final int BINARY = 3;
    final int UNARY = 4;
    final int BRACKET = 5;

    Term terms[];
    Term postFix[];
    Stack stack;

    int postFixTerms;
    boolean invalid;
    boolean parathensisMismatch;
    EquationEvaluation(String str)
    {
        equation = str;
        invalid=false;
        parathensisMismatch=false;
        parse = new String[100];
        numOfTerms=0;
        postFixTerms = 0;
        parseEquation();
        convertToPostFix();
        for (int i=0;i<numOfTerms;i++)
        {
            //System.out.println(parse[i]);
        }
        //System.out.println("now post fix");
        for (int i=0;i<postFixTerms;i++)
        {
            //System.out.println(postFix[i]);
        }
    }

    void parseEquation()
    {
        byte temp[]=equation.getBytes();

        int a = 0;
        for(int i=0;i<temp.length;i++)
        {
            if(temp[i]=='(')
            {
                a++;
            }
            else if(temp[i]==')')
            {
                a--;
            }
            if(a<0)
            {
                parathensisMismatch=true;
                return;
            }

        }
        if(a!=0)
        {
            parathensisMismatch = true;
            return;
        }

        byte b[] = new byte[3000];
        int k=0;
        for(int i=0;i<temp.length;i++)
        {
            if(temp[i]!=' ')
            {
                b[k]=temp[i];
                k++;
            }
        }
        int byteLength = k;
        int j=0;
        k=0;
        for(int i=0;i<byteLength;i++)
        {
            if((b[i]=='(')||(b[i]==')')||(b[i]=='+')||(b[i]=='-')||(b[i]=='*')||(b[i]=='/')||(b[i]=='^'))
            {
                if(j<i)
                {
                    parse[k]=new String(b,j,i-j);
                    k++;
                }
                parse[k]=new String(b,i,1);
                k++;
                j=i+1;
            }
            else if (i==byteLength-1)
            {
                parse[k]=new String(b,j,i-j+1);
                k++;
                j=i+1;
            }
        }

        numOfTerms = k;

        terms = new Term[numOfTerms];
        for(int i=0;i<numOfTerms;i++)
        {
            terms[i]= new Term();
            if((parse[i].equals("+"))||(parse[i].equals("-"))||(parse[i].equals("*"))||(parse[i].equals("/"))||(parse[i].equals("^")))
            {
                terms[i]= new Term();
                terms[i].type=BINARY;
                terms[i].value=0;
                terms[i].str = parse[i];
                if(parse[i].equals("+")||(parse[i].equals("-")))
                {
                    terms[i].priority=1;
                }
                else if(parse[i].equals("*"))
                {
                    terms[i].priority = 2;
                }
                else if(parse[i].equals("/"))
                {
                    terms[i].priority = 3;
                }
                else if(parse[i].equals("^"))
                {
                    terms[i].priority = 4;
                }
            }
            else if((parse[i].equals("x"))||(parse[i].equals("y"))||(parse[i].equals("z")))
            {
                terms[i].type=VARIBLE;
                terms[i].str=parse[i];
                terms[i].value=0;
                terms[i].priority=0;
            }
            else if((parse[i].equals("u"))||(parse[i].equals("v"))||(parse[i].equals("t")))
            {
                terms[i].type=VARIBLE;
                terms[i].value=0;
                terms[i].priority=0;

                if(parse[i].equals("u")||parse[i].equals("t"))
                {
                    terms[i].str="x";
                }
                else if(parse[i].equals("v"))
                {
                    terms[i].str="y";
                }
            }
            else if(parse[i].equals("e"))
            {
                float value = (float)(Math.exp(1));

                terms[i].str=parse[i];
                terms[i].value=value;
                terms[i].type=CONSTANT;
                terms[i].priority=0;
            }
            else if((parse[i].equals("("))||(parse[i].equals(")")))
            {
                terms[i].type=BRACKET;
                terms[i].str=parse[i];
                terms[i].value=0;
                terms[i].priority=0;
            }
            else {
                try {
                    float value = Float.valueOf(parse[i]);
                    terms[i].str=parse[i];
                    terms[i].value=value;
                    terms[i].type=CONSTANT;
                    terms[i].priority=0;
                }catch (Exception e)
                {
                    terms[i].type=UNARY;
                    terms[i].priority=5;
                    terms[i].value=0;
                    terms[i].str=parse[i];
                }
            }
        }
        for(int i=0;i<numOfTerms;i++)
        {
            if(terms[i].str.equals("-"))
            {
                if((i==0))
                {
                    terms[i].type=UNARY;
                    terms[i].priority=5;
                }
                else if((terms[i-1].type!=VARIBLE)&&(terms[i-1].type!=CONSTANT))
                {
                    terms[i].type=UNARY;
                    terms[i].priority=5;
                }
            }
        }
    }
    void convertToPostFix()
    {
        if(parathensisMismatch)
        {
            //System.out.println("parenthesis mismatch");
            return;
        }
        postFix = new Term[numOfTerms];
        stack = new Stack(numOfTerms);
        int k=0;
        for(int i=0;i<numOfTerms;i++)
        {
            Term term = new Term();
            term = terms[i];
            if((term.type==VARIBLE)||(term.type==CONSTANT))
            {
                postFix[k]=term;
                k++;
            }
            else if(term.type==BRACKET)
            {
                if(term.str.equals("("))
                {
                    stack.push(term);
                }
                else{
                    while (true)
                    {
                        Term temp = stack.pop();
                        if(temp.type==BRACKET)
                        {
                            break;
                        }
                        postFix[k]=temp;
                        k++;
                    }
                }
            }
            else {
                if(stack.isEmpty())
                {
                    stack.push(term);
                }
                else if(stack.top().priority<=term.priority)
                {
                    stack.push(term);
                }
                else {
                    while (!stack.isEmpty())
                    {
                        if(stack.top().type==BRACKET)
                        {
                            break;
                        }
                        Term temp = stack.pop();
                        postFix[k]=temp;
                        k++;
                    }
                    stack.push(term);
                }
            }
        }
        while (!stack.isEmpty())
        {
            Term temp = stack.pop();
            postFix[k]=temp;
            k++;
        }
        postFixTerms=k;
        stack.clear();
    }

    float evaluate(float x)
    {
        return eval(x,0,0);
    }
    float evaluate(float x,float y)
    {
        return eval(x,y,0);
    }
    float evaluate(float x,float y,float z)
    {
        return eval(x,y,z);
    }

    float eval(float x,float y,float z)
    {
        float ans = 0;
        stack = new Stack(postFixTerms);
        for(int i=0;i<postFixTerms;i++)
        {
            Term term = postFix[i];
            if((term.type==BINARY))
            {
                Term temp;
                temp = stack.pop();
                if(temp==null)
                {
                    invalid = true;
                    //System.out.println("invalid 1");
                    return 0;
                }
                float v2 = temp.value;

                temp = stack.pop();
                if(temp==null)
                {
                    invalid = true;
                    //System.out.println("invalid 2");
                    return 0;
                }
                float v1 = temp.value;

                float result = getResult(term.str,v1,v2);
                Term t = new Term();
                t.type=CONSTANT;
                t.value = result;
                stack.push(t);
            }
            else if (term.type==UNARY)
            {
                Term temp;
                temp = stack.pop();
                if(temp==null)
                {
                    invalid = true;
                    //System.out.println("invalid 3");
                    return 0;
                }
                float v = temp.value;
                float result = getResult(term.str,v);
                Term t = new Term();
                t.type=CONSTANT;
                t.value = result;
                stack.push(t);
            }
            else {
                if(term.type==VARIBLE)
                {
                    if(term.str.equals("x"))
                    {
                        term.value = x;
                    }
                    else if(term.str.equals("y"))
                    {
                        term.value = y;
                    }
                    else if(term.str.equals("z"))
                    {
                        term.value = z;
                    }
                }
                stack.push(term);
            }
        }
        if(stack.getSize()!=1)
        {
            invalid=true;
            //System.out.println("invalid 4");
            return 0;
        }
        ans = stack.top().value;
        return ans;
    }

    float getResult(String operator, float v)
    {
        float result = 0;

        if(operator.equals("-"))
        {
            result =(float)((-1)*v);
        }
        if(operator.equals("sin"))
        {
            result = (float)( Math.sin(v));
        }
        else if(operator.equals("cos"))
        {
            result = (float)( Math.cos(v));
        }
        else if(operator.equals("tan"))
        {
            result = (float)( Math.tan(v));
        }
        else if(operator.equals("asin"))
        {
            result = (float)( Math.asin(v));
        }
        else if(operator.equals("acos"))
        {
            result = (float)( Math.acos(v));
        }
        else if(operator.equals("atan"))
        {
            result = (float)( Math.atan(v));
        }
        else if(operator.equals("sinh"))
        {
            result = (float)( Math.sinh(v));
        }
        else if(operator.equals("cosh"))
        {
            result = (float)( Math.cosh(v));
        }
        else if(operator.equals("tanh"))
        {
            result = (float)( Math.tanh(v));
        }
        else if(operator.equals("sqrt"))
        {
            result = (float)( Math.sqrt(v));
        }
        else if(operator.equals("cbrt"))
        {
            result = (float)( Math.cbrt(v));
        }
        else if(operator.equals("ln"))
        {
            result = (float)( Math.log(v));
        }
        else if(operator.equals("log"))
        {
            result = (float)( Math.log10(v));
        }
        else if(operator.equals("abs"))
        {
            result = (float)( Math.abs(v));
        }
        else if(operator.equals("exp"))
        {
            result = (float)( Math.exp(v));
        }
        return result;
    }
    float getResult(String operator , float v1,float v2)
    {
        float result = 0;

        if(operator.equals("+"))
        {
            result = v1+v2;
        }
        else if(operator.equals("-"))
        {
            result = v1-v2;
        }
        else if(operator.equals("*"))
        {
            result = v1*v2;
        }
        else if(operator.equals("/"))
        {
            result = v1/v2;
        }
        else if(operator.equals("^"))
        {
            result = (float) (Math.pow(v1,v2));
        }
        return result;
    }
    class Term
    {
        int type;
        int priority;
        float value;
        String str;

        public String toString()
        {
            String t="";

            if(type == CONSTANT)
            {
                t+= value+ " ---> constant";
            }
            else if(type == VARIBLE)
            {
                t+= str+" ---> variable";
            }
            else if(type == BINARY)
            {
                t+= str+" ---> binary operator, priority :"+priority;
            }
            else if(type == UNARY)
            {
                t+= str+" ---> unary operator, priority :"+priority;
            }
            else if(type == BRACKET) {
                t += str + " ---> parenthesis";
            }

            return t;
        }

    }

    class Stack
    {
        int top;
        int size;
        Term arr[];
        int i;
        Stack(int n)
        {
            i=0;
            size=n;
            arr = new Term[size];
        }

        void push(Term t)
        {
            if(!isFull()) {
                arr[i] = t;
                i++;
            }
        }
        Term pop()
        {
            if(!isEmpty())
            {
                i--;
                return arr[i];
            }
            return null;
        }

        Term top()
        {
            if(!isEmpty())
            {
                return arr[i-1];
            }
            return null;
        }

        boolean isEmpty()
        {
            if(i==0)
            {
                return true;
            }
            return false;
        }
        boolean isFull()
        {
            if(i==size)
            {
                return true;
            }
            return false;
        }
        int getSize()
        {
            return i;
        }
        void clear()
        {
            i=0;
        }
    }

}

