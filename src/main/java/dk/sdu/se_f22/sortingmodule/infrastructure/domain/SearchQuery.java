package dk.sdu.se_f22.sortingmodule.infrastructure.domain;

import java.util.ArrayList;

/**
 * Query class, that holds information about a search query from the user.
 */
public class SearchQuery {
    /**
     * Pagination information. 2 places. [page, page size]
     */
    int[] pagination;

    /**
     * List of range filters
     */
    ArrayList<String[]> range;

    /**
     * List of categories to filter by
     */
    ArrayList<Integer> category;

    /**
     * Scoring method id
     */
    int scoring;

    public SearchQuery() {
        this.pagination = new int[2];
        this.range = new ArrayList<>();
        this.category = new ArrayList<>();
        this.scoring = 0;
    }

    /**
     * Set the categories to filter by
     * 
     * @param categories Arraylist with ids of categories
     */
    public void setCategory(ArrayList<Integer> categories) {
        this.category = categories;
    }

    /**
     * Add a new category to filter by
     * 
     * @param category Id of category to filter by
     */
    public void addCategory(int category) {
        this.category.add(category);
    }

    /**
     * Reset category filtering
     */
    public void clearCategory() {
        category = new ArrayList<>();
    }

    /**
     * Add a new range to filter by
     * 
     * @param rangeId The id of the range to filter by
     * @param startRange The start of the range
     * @param endRange The end of the range
     */
    public void addRange(String rangeId, String startRange, String endRange) {

        this.range.add(new String[]{rangeId, startRange, endRange});

    }

    /**
     * Reset the range filtering
     */
    public void clearRange() {
        this.range.clear();
    }

    /**
     * Set the pagination information
     * 
     * @param page Current page to query
     * @param pageSize The amount of hits to return
     */
    public void setPagination(int page, int pageSize) {
        this.pagination[0] = page;
        this.pagination[1] = pageSize;
    }

    /**
     * Set the scoring method
     * 
     * @param scoring The id of the scoring method
     */
    public void setScoring(int scoring) {
        this.scoring = scoring;
    }

    public ArrayList<Integer> getCategory() {
        return this.category;
    }

    public ArrayList<String[]> getRange() {
        return this.range;
    }

    public int getScoring() {
        return this.scoring;
    }

    public int[] getPagination() {
        return this.pagination;
    }
}