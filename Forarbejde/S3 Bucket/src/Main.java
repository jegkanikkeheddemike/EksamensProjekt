
class Main {
    public static void main(String[] args) {
        DBInterface dbi = new DBInterface();
        dbi.connect();
        dbi.printBuckets();
    }
}