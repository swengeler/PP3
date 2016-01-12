/*public int getNrStates(int csLength, int csWidth, int csHeight) {
    int[] nrStates;
    int totalNr = 0;
    if (this.length == this.width && this.length == this.height) {
        nrStates = new int[2];
        int nrStates1 += (csLength - (this.length - 1)) * (csWidth - (this.width - 1)) * (csHeight - (this.height - 1));
    } else if (this.length == this.width) {
        nrStates = new int[4];
        int nrStates1 += (csLength - (this.length - 1)) * (csWidth - (this.width - 1)) * (csHeight - (this.height - 1));
        int nrStates2 += (csLength - (this.height - 1)) * (csWidth - (this.width - 1)) * (csHeight - (this.length - 1));
        int nrStates3 += (csLength - (this.width - 1)) * (csWidth - (this.length - 1)) * (csHeight - (this.height - 1));
    } else if (this.length == this.height) {
        nrStates = new int[4];
        int nrStates1 += (csLength - (this.length - 1)) * (csWidth - (this.width - 1)) * (csHeight - (this.height - 1));
        int nrStates2 += (csLength - (this.length - 1)) * (csWidth - (this.height - 1)) * (csHeight - (this.width - 1));
        int nrStates3 += (csLength - (this.height - 1)) * (csWidth - (this.width - 1)) * (csHeight - (this.length - 1));
    } else if (this.width == this.height) {
        nrStates = new int[4];
        int nrStates1 += (csLength - (this.length - 1)) * (csWidth - (this.width - 1)) * (csHeight - (this.height - 1));
        int nrStates2 += (csLength - (this.height - 1)) * (csWidth - (this.width - 1)) * (csHeight - (this.length - 1));
        int nrStates3 += (csLength - (this.length - 1)) * (csWidth - (this.height - 1)) * (csHeight - (this.width - 1));
    } else {
        nrStates = new int[7];
        int nrStates1 += (csLength - (this.length - 1)) * (csWidth - (this.width - 1)) * (csHeight - (this.height - 1));
        int nrStates2 += (csLength - (this.length - 1)) * (csWidth - (this.height - 1)) * (csHeight - (this.width - 1));
        int nrStates3 += (csLength - (this.height - 1)) * (csWidth - (this.width - 1)) * (csHeight - (this.length - 1));
        int nrStates4 += (csLength - (this.width - 1)) * (csWidth - (this.length - 1)) * (csHeight - (this.height - 1));
        int nrStates5 += (csLength - (this.length - 1)) * (csWidth - (this.height - 1)) * (csHeight - (this.width - 1));
        int nrStates6 += (csLength - (this.height - 1)) * (csWidth - (this.width - 1)) * (csHeight - (this.length - 1));
    }
    return nrStates;
}
*/
