package at.jku.dke.etutor.modules.pm.AlphaAlgo;

public class FootprintMatrixCell {

    // OBJECT FIELD
    private OrderingRelation sign;

    // CONSTRUCTOR
    public FootprintMatrixCell(){
        // empty FootprintMatrix at the beginning
        this.sign = null;
    }

    // METHODS
    //getter
    public OrderingRelation getSign() {
        return sign;
    }

    //setter
    public void setSign(OrderingRelation sign) {
        this.sign = sign;
    }

    /**
     * toString() method that is used for printing the single cells
     * at start, all cells are printed empty
     * if cell is assigned a value, then the respective sign is printed
     */
    @Override
    public String toString() {
       if(sign == null){
           return " ";
       }else{
           return Character.toString(sign.getRelation());
       }
    }
}
