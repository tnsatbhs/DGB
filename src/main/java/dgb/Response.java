package dgb;

class Response {

    private Integer gradebook_id;
    private String statusMessage;

    public void setId(Integer id){
        this.gradebook_id = id;
    }

    public Integer getId() {
        return this.gradebook_id;
    }

    public void setStatus(String status){
        this.statusMessage = status;
    }

    public String getStatus(){
        return this.statusMessage;
    }
    public Response(Integer id, String message) {
        this.setId(id);
        this.setStatus(message);

    }

    // getters, setters, etc
}

