
package com.kelvindu.learning.scenema.model.trailerlist;

import java.util.List;

/*
* if you ever wondering what the fuck is these stuff in models folder, this is basically just some class exported
* from json format of some api, all you need to do is go to
* http://www.jsonschema2pojo.org/
* as for what to do next? you probably already figure it out by now
*
* */

public class TrailerList {

    private Integer id;
    private List<ResultTrailer> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ResultTrailer> getResults() {
        return results;
    }

    public void setResults(List<ResultTrailer> results) {
        this.results = results;
    }

}
