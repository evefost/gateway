package com.xhg.gateway.vo;

import java.util.Collections;
import java.util.List;

public class PagerResult<E> {

    public static <E> PagerResult<E> empty(){
        return new PagerResult<>(0, Collections.emptyList());
    }

    public static <E> PagerResult<E> of(int total,List<E> list){
        return new PagerResult<>(total,list);
    }

    private long total;

    private List<E> items;

    private int pageSize;
    private int currentPage;
    private int totalPages;
    
    public int getPageSize() {
    	int defaultSize = 10;
		return pageSize==0?defaultSize:pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}



	public PagerResult() {
    }

    public PagerResult(int total, List<E> data) {
        this.total = total;
        this.items = data;
    }

    public long getTotal() {
        return total;
    }

    public PagerResult<E> setTotal(long total) {
        this.total = total;
        return this;
    }

    public List<E> getList() {
        return items;
    }

    
    public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	@SuppressWarnings("unchecked")
	public <T> PagerResult<E> setList(List<T> list) {
        this.items = (List<E>) list;
        return this;
    }
	


}