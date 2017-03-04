
package com.kelvindu.learning.scenema.model.movielist;

import java.util.List;

/*
* if you ever wondering what the fuck is these stuff in models folder, this is basically just some class exported
* from json format of some api, all you need to do is go to
* http://www.jsonschema2pojo.org/
* as for what to do next? you probably already figure it out by now
*
* */

public class MovieList {

    private Integer page;
    private List<Result> results = null;
    private Integer totalResults;
    private Integer totalPages;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

}
