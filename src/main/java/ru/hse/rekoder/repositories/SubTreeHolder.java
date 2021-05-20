package ru.hse.rekoder.repositories;

import org.springframework.data.annotation.Id;

import java.util.List;

public class SubTreeHolder {
    public Integer id;

    public List<Integer> res;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getRes() {
        return res;
    }

    public void setRes(List<Integer> res) {
        this.res = res;
    }
}
