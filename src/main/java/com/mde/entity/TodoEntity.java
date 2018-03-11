package com.mde.entity;

import com.mde.type.TodoStatusType;


public class TodoEntity {

 
    private long id;

   
    private TodoStatusType status;

    private String name;

    public TodoEntity(){
    	
    }

	public TodoEntity(String name, TodoStatusType status) {
		super();
		this.status = status;
		this.name = name;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TodoStatusType getStatus() {
        return status;
    }

    public void setStatus(TodoStatusType status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TodoEntity that = (TodoEntity) o;

        if (id != that.id) return false;
        if (status != that.status) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TodoEntity{" +
                "id=" + id +
                ", status=" + status +
                ", name='" + name + '\'' +
                '}';
    }

}
