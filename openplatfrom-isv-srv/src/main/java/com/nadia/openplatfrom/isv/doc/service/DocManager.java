package com.nadia.openplatfrom.isv.doc.service;


import com.nadia.openplatfrom.isv.doc.domain.DocInfo;
import com.nadia.openplatfrom.isv.doc.domain.DocItem;

import java.util.Collection;

public interface DocManager {

    void load();

    DocItem get(String method, String version);

    DocItem get(String method);

    DocInfo getByTitle(String title);

    Collection<DocInfo> listAll();

    Collection<DocItem> get();
}
