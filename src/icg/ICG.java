package icg;

import lexical.Token;
import parser.Edge;
import semantic.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ICG {
    private static final int MAX_LINE_NUMBER = 500;
    private AddressGenerator addressGenerator;
    private int curline;
    private Map<Integer,TAC> programBlock  = new HashMap<>();
    private SymbolTable curSymbolTable;
    private Stack<Data> semanticStack = new Stack<>();

    public ICG(AddressGenerator addressGenerator) {
        this.addressGenerator = addressGenerator;
    }


    public void action (ICGTokenType icgTokenType, Token curToken,SymbolTable in) {
        curSymbolTable = in;

        switch (icgTokenType) {
            case MAIN:
                main();
                break;
            case END_OF_FILE:
                endOfFile();
                break;
            case ASSIGN_FIRST_LINE:
                assignFirstLine();
                break;
            case POP:
                semanticStack.pop();
                break;
            case JP_FIRST:
                jpFirst();
                break;
            case JP_END:
                jpEnd();
                break;
            case IF_JP:
                ifJp();
                break;
            case IF_SAVE_JP:
                ifSaveJp();
                break;
            case IF_SAVE:
                ifSave();
                break;
            case WHILE_START:
                whileStart();
                break;
            case WHILE_JP:
                whileJp();
                break;
            case WHILE_END:
                whileEnd();
                break;
            case RETURN_FUNC:
                returnFunc();
                break;
            case PUSH_NULL:
                pushNull();
                break;
            case SWITCH_START:
                switchStart();
                break;
            case SWITCH_EXTRA_JP:
                switchExtraJp();
                break;
            case SWITH_END:
                switchEnd();
                break;
            case SWITCH_SAVE:
                switchSave();
                break;
            case SWITCH_SAVE_JP:
                switchSaveJp();
                break;
            case JUMPER:
                jumper();
                break;
            case PID:
                pid(curToken);
                break;
            case INDEXING:
                indexing();
                break;
            case CALING_FUNCTION:
                callingFunction();
                break;
            case ASSIGN_PUSH:
                assignPush();
                break;
            case PUSH_ZERO:
                pushZero();
                break;
            case PUSH_RELOP:
                pushRelop(curToken);
                break;
            case ADDOP:
                addop(curToken);
                break;
            case CALCULATE:
                calculate();
                break;
            case MULT:
                mult();
                break;
            case GHARINE:
                gharine();
                break;
            case PUSH_NUM:
                pushNum(curToken);
                break;
            case START_ARG:
                startArg();
                break;
            case ASSIGN_INPUT:
                assignInput();
                break;
        }

    }

    private void main(){
        semanticStack.push(new Data(curline));
        curline++;
        createOutput();
    }

    private void createOutput(){

        SymbolTable output = curSymbolTable.getFunction("output");
        output.setStartLine(curline);
        System.out.println(curline);
        programBlock.put(curline, new TAC(TACType.PRINT,new Data(output.getContents().get(2).getAddress(),false)));
        curline++;
        programBlock.put(curline,new TAC(TACType.JP,new Data(output.getContents().get(0).getAddress(),false)));
        curline++;

    }

    private void endOfFile() {


        SymbolTable main = curSymbolTable.getFunction("main");
        TAC tac = new TAC(TACType.JP, new Data(main.getStartLine()));
        programBlock.put(semanticStack.pop().getValue(), tac);
    }

    private void assignFirstLine(){
        curSymbolTable.setStartLine(curline);
    }

    private void jpFirst(){
        ///todo getparent...
        int startLine;
        if(curSymbolTable.getSymbolTableType() == SymbolTableType.WHILE){
            startLine = curSymbolTable.getStartLine();
        }else{
            startLine = curSymbolTable.getParent().getStartLine();
        }

        TAC tac = new TAC(TACType.JP,new Data(startLine));
        programBlock.put(curline,tac);
        curline++;
    }

    private void jpEnd(){
        ///todo getparent..
        int endLine = curSymbolTable.find("break").getAddress();
        TAC tac = new TAC(TACType.JP,new Data(endLine,false));
        programBlock.put(curline,tac);
        curline++;

    }

    private void ifJp(){
        int t = addressGenerator.getTemp();
        TAC tac = new TAC(TACType.EQ,semanticStack.pop(),new Data(0),t);
        programBlock.put(curline,tac);
        curline++;
        semanticStack.push(new Data(curline));
        semanticStack.push(new Data(t,false));
    }

    private  void ifSaveJp(){
        curline++;
        TAC tac = new TAC(TACType.JPF,semanticStack.pop(),new Data(curline));
        programBlock.put(semanticStack.pop().getValue(),tac);
        semanticStack.push(new Data(curline - 1));
    }

    private void ifSave(){
        TAC tac = new TAC(TACType.JP,new Data(curline));
        programBlock.put(semanticStack.pop().getValue(),tac);
    }

    private void whileStart(){

        semanticStack.push(new Data(curline));
        curline++;
        curSymbolTable.setStartLine(curline);
    }

    private void whileJp(){

        programBlock.put(curline,new TAC(TACType.JPF,semanticStack.pop(),new Data(curSymbolTable.getContents().get(0).getAddress(),false)));
        curline++;
    }

    private void whileEnd(){

        programBlock.put(curline,new TAC(TACType.JP,new Data(curSymbolTable.getStartLine())));
        curline++;
        programBlock.put(semanticStack.pop().getValue(),new TAC(TACType.ASSIGN,new Data(curline),new Data(curSymbolTable.getContents().get(0).getAddress(),false)));

        //TODO curSymbol.getparent or curSymbol
    }

    private void pushNull(){
        semanticStack.push(null);
    }

    private void returnFunc () {

        TAC tac = new TAC(TACType.ASSIGN, semanticStack.pop(), new Data(curSymbolTable.getParent().getContents().get(1).getAddress()));
        programBlock.put(curline,tac);
        curline++;
        tac = new TAC(TACType.JP,new Data(curSymbolTable.getParent().getContents().get(0).getAddress(),false));
        programBlock.put(curline,tac);
        curline++;
    }

    private void  switchStart(){

        semanticStack.push(new Data(curline));
        curline++;
        curSymbolTable.setStartLine(curline);
    }

   private void switchExtraJp(){

       // TODO: 6/30/19 experession
        semanticStack.push(new Data(0));
        semanticStack.push(new Data(curline));
        semanticStack.push(null);
        curline++;
   }

   private void jumper(){
        semanticStack.push(new Data(curline));
        curline++;
   }

   private void switchSaveJp(){
        Data number = semanticStack.pop();
        Data jumpLine = semanticStack.pop();
        if(jumpLine != null)
            programBlock.put(jumpLine.getValue(),new TAC(TACType.JP,new Data(curline + 2)));

        programBlock.put(semanticStack.pop().getValue(),new TAC(TACType.JPF,semanticStack.pop(),new Data(curline)));
        int t = addressGenerator.getTemp();
        programBlock.put(curline, new TAC(TACType.EQ,number,semanticStack.peek(),t));
        curline++;
        semanticStack.push(new Data(t,false));
        semanticStack.push(new Data(curline));
        curline++;
   }

   private void switchSave(){
       Data jumpLine = semanticStack.pop();
       programBlock.put(semanticStack.pop().getValue(),new TAC(TACType.JPF,semanticStack.pop(),new Data(curline)));
   }

   private void switchEnd(){
       semanticStack.pop();
       programBlock.put(semanticStack.pop().getValue(),new TAC(TACType.ASSIGN,new Data(curline),new Data(curSymbolTable.getContents().get(0).getAddress(),false)));
   }

   private void pid(Token token){
        semanticStack.push(new Data(token.getToken()));
   }

   private void pushZero(){
        semanticStack.push(null);
   }

   private void indexing() {

       Data index = semanticStack.pop();
       Data var = semanticStack.pop();


       //TODO why self or parent!?
       int address = curSymbolTable.find(var.getLabel()).getAddress();
       AttributeType type = curSymbolTable.find(var.getLabel()).getAttributeType();
       if(index == null) {
           semanticStack.push(new Data(address, false));
       }
       else {
           int temp = addressGenerator.getTemp();
           programBlock.put(curline, new TAC(TACType.MULT,new Data(4), index,temp));
           curline++;
           var.setValue(address);
           if(type == AttributeType.POINTER){
               var.setPointer(false);
               var.setConstant(false);
           }
           programBlock.put(curline, new TAC(TACType.ADD,new Data(temp,false),var,temp));
           curline++;
           semanticStack.push(new Data(temp, false,true));
       }


   }

   private void pushRelop(Token curToken){
       semanticStack.push(new Data(curToken.getToken()));
   }

   private void calculate(){
        Data operand2 =  semanticStack.pop();
        Data operator = semanticStack.pop();
        Data operand1 = semanticStack.pop();
        Data result =  new Data(addressGenerator.getTemp(),false);
        if(operator.getLabel().equals("==")){
            programBlock.put(curline,new TAC(TACType.EQ,operand1,operand2,result.getValue()));
        }else if (operator.getLabel().equals("<") ){
            programBlock.put(curline,new TAC(TACType.LT,operand1,operand2,result.getValue()));
        }else if (operator.getLabel().equals("+")) {
           programBlock.put(curline, new TAC(TACType.ADD, operand1, operand2, result.getValue()));
       } else {
           programBlock.put(curline, new TAC(TACType.SUB, operand1, operand2, result.getValue()));
       }
        curline++;
        semanticStack.push(result);
   }

   private void addop(Token curToken){
       semanticStack.push(new Data(curToken.getToken()));
   }

   private void mult(){
       Data operand2 =  semanticStack.pop();
       Data operand1 = semanticStack.pop();

       Data result =  new Data(addressGenerator.getTemp(),false);
       programBlock.put(curline,new TAC(TACType.MULT,operand1,operand2,result.getValue()));
       curline++;
       semanticStack.push(result);
   }

   private void assignPush(){
        programBlock.put(curline , new TAC(TACType.ASSIGN,semanticStack.pop(),semanticStack.peek()));
        curline++;
   }

   private void pushNum(Token token){
        semanticStack.push(new Data(Integer.valueOf(token.getToken())));
        //todo handle error in semantic
   }

   private void gharine(){
        Data result =  new Data(addressGenerator.getTemp(),false);
        programBlock.put(curline,new TAC(TACType.MULT,semanticStack.pop(),result));
        curline++;
        semanticStack.push(result);
   }

   private void startArg(){
        int counter = 0;
        semanticStack.push(new Data(counter));
        printStack();
   }

   private void assignInput(){
       String functionName = semanticStack.get(semanticStack.size() -3).getLabel();
        Data counter  = semanticStack.get(semanticStack.size() -2);
        Attribute address = curSymbolTable.getFunction(functionName).getContents().get(counter.getValue() + 2);

        Data curData = semanticStack.pop();
        if(address.getAttributeType() == AttributeType.POINTER){
            curData.setPointer(false);
            curData.setConstant(true);
        }
        programBlock.put(curline,new TAC(TACType.ASSIGN,curData,new Data(address.getAddress(),false)));
        curline++;
        counter.setValue(counter.getValue() + 1);
   }

    private void callingFunction(){
        printStack();
        String functionName = semanticStack.get(semanticStack.size() -2).getLabel();
        SymbolTable function = curSymbolTable.getFunction(functionName);
        int address = function.getContents().get(0).getAddress();
        programBlock.put(curline,new TAC(TACType.ASSIGN,new Data(curline + 2 ),new Data(address,false)));
        curline++;
        int number = function.getStartLine();
        programBlock.put(curline,new TAC(TACType.JP,new Data(number)));
        curline++;
        address = function.getContents().get(1).getAddress();
        int result = addressGenerator.getTemp();
        if(function.getContents().get(1).getAttributeType() != AttributeType.VOID) {
            programBlock.put(curline, new TAC(TACType.ASSIGN, new Data(address, false), new Data(result, false)));
            curline++;
        }
        semanticStack.pop();
        semanticStack.pop();
        semanticStack.push(new Data(result,false));
    }

    public Map<Integer, TAC> getProgramBlock() {
        return programBlock;
    }

    private void printStack(){
        System.out.println("---------start");
        for (Data data : semanticStack) {
            if (data == null){
                System.out.println(data);
            }
            else if(data.getLabel() == null)
                System.out.println(data);
            else
                System.out.println(data.getLabel());
        }
        System.out.println("---------end");
    }
}

