package icg;

import lexical.Token;
import parser.Edge;
import semantic.AddressGenerator;
import semantic.SymbolTable;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ICG {
    private static final int MAX_LINE_NUMBER = 500;
    private AddressGenerator addressGenerator;
    private int curline;
    private Map<Integer,TAC> programBlock  = new HashMap<>();
    private SymbolTable curSymbolTable;
    private Stack<Data> semanticStack;

    public ICG(AddressGenerator addressGenerator) {
        this.addressGenerator = addressGenerator;
    }


    public void action (Edge edge, Token curToken) {
        ICGTokenType icgTokenType = edge.getIcgTokenType();
        switch (icgTokenType) {

            case MAIN:
                main();
                break;
            case ENF_OF_FILE:
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

    public void main(){
        semanticStack.push(new Data(curline));
        curline++;
    }

    public void endOfFile(){
       SymbolTable main =  curSymbolTable.getFunction("main");
       TAC tac = new TAC(TACType.JP,new Data(main.getStartLine()));
       programBlock.put(semanticStack.pop().getValue(),tac);
    }

    public void assignFirstLine(){
        curSymbolTable.setStartLine(curline);
    }

    public void jpFirst(){
        int startLine = curSymbolTable.getParent().getStartLine();
        TAC tac = new TAC(TACType.JP,new Data(startLine));
        programBlock.put(curline,tac);
        curline++;
    }

    public void jpEnd(){
        int endLine = curSymbolTable.getParent().find("break").getAddress();
        TAC tac = new TAC(TACType.JP,new Data(endLine,false));
        programBlock.put(curline,tac);
        curline++;

    }

    public void ifJp(){
        int t = addressGenerator.getTemp();
        TAC tac = new TAC(TACType.EQ,semanticStack.pop(),new Data(0),t);
        programBlock.put(curline,tac);
        curline++;
        semanticStack.push(new Data(curline));
        semanticStack.push(new Data(t,false));
    }

    public  void ifSaveJp(){
        curline++;
        TAC tac = new TAC(TACType.JPF,semanticStack.pop(),new Data(curline));
        programBlock.put(semanticStack.pop().getValue(),tac);
        semanticStack.push(new Data(curline - 1));
    }

    public void ifSave(){
        TAC tac = new TAC(TACType.JP,new Data(curline));
        programBlock.put(semanticStack.pop().getValue(),tac);
    }

    public void whileStart(){

        semanticStack.push(new Data(curline));
        curline++;
        curSymbolTable.setStartLine(curline);
    }

    public void whileJp(){
        programBlock.put(curline,new TAC(TACType.JP,new Data(curSymbolTable.getContents().get(0).getAddress(),false)));
        curline++;
    }

    public void whileEnd(){
        programBlock.put(curline,new TAC(TACType.JP,new Data(curSymbolTable.getStartLine())));
        curline++;
        programBlock.put(semanticStack.pop().getValue(),new TAC(TACType.ASSIGN,new Data(curline),new Data(curSymbolTable.getContents().get(0).getAddress(),false)));

        //TODO curSymbol.getparent or curSymbol
    }

    public void pushNull(){
        semanticStack.push(null);
    }

    public void returnFunc () {

        TAC tac = new TAC(TACType.ASSIGN, semanticStack.pop(), new Data(curSymbolTable.getParent().getContents().get(1).getAddress()));
        programBlock.put(curline,tac);
        curline++;
        tac = new TAC(TACType.JP,new Data(curSymbolTable.getParent().getContents().get(0).getAddress(),false));
        programBlock.put(curline,tac);
        curline++;
    }

    public void  switchStart(){
        semanticStack.push(new Data(curline));
        curline++;
        curSymbolTable.setStartLine(curline);
    }

   public void switchExtraJp(){

       // TODO: 6/30/19 experession
        semanticStack.push(new Data(0));
        semanticStack.push(new Data(curline));
        semanticStack.push(new Data(0));
        curline++;
   }

   public void jumper(){
        semanticStack.push(new Data(curline));
        curline++;
   }

   public void switchSaveJp(){
        Data number = semanticStack.pop();
        Data jumpLine = semanticStack.pop();
        programBlock.put(jumpLine.getValue(),new TAC(TACType.JP,new Data(curline + 2)));
        programBlock.put(semanticStack.pop().getValue(),new TAC(TACType.JPF,semanticStack.pop(),new Data(curline)));
        int t = addressGenerator.getTemp();
        programBlock.put(curline, new TAC(TACType.EQ,number,semanticStack.peek(),t));
        curline++;
        semanticStack.push(new Data(t,false));
        semanticStack.push(new Data(curline));
        curline++;
   }

   public void switchSave(){
       Data number = semanticStack.pop();
       Data jumpLine = semanticStack.pop();
       programBlock.put(semanticStack.pop().getValue(),new TAC(TACType.JPF,semanticStack.pop(),new Data(curline)));
       semanticStack.pop();
   }

   public void switchEnd(){
        programBlock.put(semanticStack.pop().getValue(),new TAC(TACType.ASSIGN,new Data(curline),new Data(curSymbolTable.getContents().get(0).getAddress(),false)));
   }

   public void pid(Token token){
        semanticStack.push(new Data(token.getToken()));
   }

   public void pushZero(){
        semanticStack.push(new Data(0));
   }

   public void indexing(){
        Data index = semanticStack.pop();
        Data var = semanticStack.pop();
        int address = curSymbolTable.findInSelfOrParent(var.getLabel()).getAddress();
        semanticStack.push(new Data(address + index.getValue() * 4 ,false));

   }

   public void pushRelop(Token curToken){
       semanticStack.push(new Data(curToken.getToken()));
   }

   public void calculate(){
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

   public void addop(Token curToken){
       semanticStack.push(new Data(curToken.getToken()));
   }

   private void mult(){
       Data operand2 =  semanticStack.pop();
       Data operand1 = semanticStack.pop();
       Data result =  new Data(addressGenerator.getTemp(),false);
       programBlock.put(curline,new TAC(TACType.MULT,operand1,operand2,result.getValue()));
       curline++;
   }

   private void assignPush(){
        programBlock.put(curline , new TAC(TACType.ASSIGN,semanticStack.pop(),semanticStack.peek()));
        curline++;
   }

   public void pushNum(Token token){
        semanticStack.push(new Data(Integer.valueOf(token.getToken())));
        //todo handle error in semantic
   }

   public void gharine(){
        Data result =  new Data(addressGenerator.getTemp(),false);
        programBlock.put(curline,new TAC(TACType.MULT,semanticStack.pop(),result));
        curline++;
        semanticStack.push(result);
   }

   public void startArg(){
        int counter = 0;
        semanticStack.push(new Data(counter));
   }

   public void assignInput(){
        String functionName = semanticStack.get(semanticStack.size() -3).getLabel();
        Data counter  = semanticStack.get(semanticStack.size() -2);
        int address = curSymbolTable.getFunction(functionName).getContents().get(counter.getValue() + 2).getAddress();
        programBlock.put(curline,new TAC(TACType.ASSIGN,semanticStack.pop(),new Data(address,false)));
        curline++;
        counter.setValue(counter.getValue() + 4);
   }

    public void callingFunction(){
        String functionName = semanticStack.get(semanticStack.size() -2).getLabel();
        SymbolTable function = curSymbolTable.getFunction(functionName);
        int address = function.getContents().get(0).getAddress();
        programBlock.put(curline,new TAC(TACType.ASSIGN,new Data(curline + 1),new Data(address,false)));
        curline++;
        address = function.getContents().get(1).getAddress();
        int result = addressGenerator.getTemp();
        programBlock.put(curline,new TAC(TACType.ASSIGN,new Data(address,false),new Data(result,false)));
        curline++;
        semanticStack.pop();
        semanticStack.pop();
        semanticStack.push(new Data(result,false));
    }


}

