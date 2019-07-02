package icg;

import javax.management.StringValueExp;

public class TAC {

    TACType tacType;
    Data var1;
    Data var2;
    int var3 = -1;


    public TAC(TACType tacType, Data var1) {
        this.tacType = tacType;
        this.var1 = var1;
    }

    public TAC(TACType tacType, Data var1, Data var2) {
        this.tacType = tacType;
        this.var1 = var1;
        this.var2 = var2;
    }

    public TAC(TACType tacType, Data var1, Data var2, int var3) {
        this.tacType = tacType;
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
    }

    @Override
    public String toString() {

        String str = tacType.toString();
        String lastVar = "";
        if (var3 != -1) {
            lastVar = String.valueOf(var3);
        }
        if (tacType == TACType.JPF) {
            str = "(" + str + ", " + var1.toString() + ", " + var2.getWithAddress() + ", " + ")";
            return str;
        } else if (tacType == TACType.JP) {
            str = "(" + str + ", " + var1.getWithAddress() + ", " + ", " + ")";
            return str;
        }
        if (var2 != null)
            str = "(" + str + ", " + var1.toString() + ", " + var2.toString() + ", " + lastVar + ")";
        else
            str = "(" + str + ", " + var1.toString() + ", " + ", " + lastVar + ")";
        return str;
    }
}
