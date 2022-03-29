package dk.sdu.se_f22.sortingmodule.scoring;

import java.util.List;

public interface IScoring {

    /**
     <p> Returns weight value sum for multiple categories </p>
     */
    List<Product> scoreSort(List<Product> input);

    /**
     <p> Returns weight value for price </p>
     * @return
     */
    List<Product> scoreSortPrice(List<Product> input);

    /**
     <p> Returns weight value for review </p>
     */
    List<Product> scoreSortReview(List<Product> input);

    /**
     <p> Returns weight value for stock </p>
     */
    List<Product> scoreSortStock(List<Product> input);

    /**
     <p> Returns weight value for release date </p>
     */
    List<Product> scoreSortDate(List<Product> input);

    /**
     <p> Returns the scores table </p>
     */
    List<String> readTable();

    /**
     <p> Updates the row with the new data </p>
     */
    void updateRow(int id, Object newValue, String column);

    /**
     <p> Deletes the row from the database </p>
     */
    void deleteRow(int id);

    /**
     <p> Creates a new row in the database </p>
     */
    void createRow(String type, double bracket, int weight);



}
