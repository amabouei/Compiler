package semantic;

public class AddressGenerator {

    private int variableAddress = 500;
    private int tempAddress = 1000;

    public int getTemp(){
        tempAddress += 4;
        return tempAddress - 4;
    }

    public int getVar(){
        variableAddress += 4;
        return variableAddress - 4;
    }

    public int getArray(int size){
        int temp = variableAddress;
        variableAddress += 4 * size;
        return temp;
    }
}
