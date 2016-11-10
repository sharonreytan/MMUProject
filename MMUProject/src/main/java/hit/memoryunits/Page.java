package hit.memoryunits;

import java.io.Serializable;
import java.util.Arrays;

public class Page<T> implements Serializable {

	private static final long serialVersionUID = 7258241415061672633L;
	private T content;
	private java.lang.Long 	pageId;
	
	
	public Page(Long id, T content) {
		this.content=content;
		this.pageId=id;
		}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((pageId == null) ? 0 : pageId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		Page<T> other = (Page<T>) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (pageId == null) {
			if (other.pageId != null)
				return false;
		} else if (!pageId.equals(other.pageId))
			return false;
		return true;
	}
	public T getContent() {return content;}
	public java.lang.Long getPageId() {return pageId;} 
	public void setContent(T content){this.content=content;}
	public void setPageId(java.lang.Long pageId) {this.pageId=pageId;}
	@Override
	public java.lang.String toString() {
		if (content instanceof byte[])
			return "pageId: " + pageId + " Content: " + Arrays.toString((byte[])content);
		else
			return "pageId: " + pageId;
		}
}
