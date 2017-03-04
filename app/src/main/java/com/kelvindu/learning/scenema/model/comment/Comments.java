
package com.kelvindu.learning.scenema.model.comment;

import java.util.List;

/*
* if you ever wondering what the fuck is these stuff in models folder, this is basically just some class exported
* from json format of some api, all you need to do is go to
* http://www.jsonschema2pojo.org/
* as for what to do next? you probably already figure it out by now
*
* */

public class Comments {

    private Integer id;
    private Integer page;
    private List<ResultComment> results = null;
    private Integer totalPages;
    private Integer totalResults;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<ResultComment> getResults() {
        return results;
    }

    public void setResults(List<ResultComment> results) {
        this.results = results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

}
