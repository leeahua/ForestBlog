package com.smile.blog.entity.custom;

import com.smile.blog.entity.Tag;

import java.io.Serializable;

/**
 * 文章标签的信息的扩展
 * Created by 言曌 on 2017/9/1.
 */
public class TagCustom extends Tag implements Serializable {
	private static final long serialVersionUID = -8810574923890223084L;
	//标签对应的文章数目
	private Integer articleCount;

	public Integer getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(Integer articleCount) {
		this.articleCount = articleCount;
	}
}
