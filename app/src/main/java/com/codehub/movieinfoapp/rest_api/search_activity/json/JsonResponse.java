package com.codehub.movieinfoapp.rest_api.search_activity.json;

import java.util.List;

public class JsonResponse {
    private int page;
    private List<JsonResultsResponse> results;
    private int total_pages;
    private int total_results;



    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<JsonResultsResponse> getResults() {
        return results;
    }

    public void setResults(List<JsonResultsResponse> results) {
        this.results = results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    @Override
    public String toString() {
        return "JsonResponse{" +
                "page=" + page +
                ", results=" + results +
                ", total_pages=" + total_pages +
                ", total_results=" + total_results +
                '}';
    }
}
