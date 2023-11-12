package model;

public class PhysicalBook extends Book{
    //might add specific physical book information, unrelated to AudioBook or EBook

    @Override
    public String toString() {
        return "Physical book" + super.toString();
    }
}
