package icg;

public class TAC {

    TACType tacType;
    Data var1;
    Data var2;
    int var3;



    public TAC(TACType tacType, Data var1) {
        this.tacType = tacType;
        this.var1 = var1;
    }

    public TAC(TACType tacType, Data var1, Data var2) {
        this.tacType = tacType;
        this.var1 = var1;
        this.var2 = var2;
    }

    public TAC(TACType tacType, Data var1, Data var2,int var3) {
        this.tacType = tacType;
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
    }

    @Override
    public String toString() {
        //TODO
        return null;
    }
}
