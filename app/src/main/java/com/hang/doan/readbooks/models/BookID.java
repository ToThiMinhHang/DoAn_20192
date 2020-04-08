package com.hang.doan.readbooks.models;

public class BookID {
    Long id_tac_gia;

    public Long getId_tac_gia() {
        return id_tac_gia;
    }

    public void setId_tac_gia(Long id_tac_gia) {
        this.id_tac_gia = id_tac_gia;
    }

    public Long getId_tac_pham() {
        return id_tac_pham;
    }

    public void setId_tac_pham(Long id_tac_pham) {
        this.id_tac_pham = id_tac_pham;
    }

    Long id_tac_pham;

    public BookID(Long id_tac_gia, Long id_tac_pham) {
        this.id_tac_gia = id_tac_gia;
        this.id_tac_pham = id_tac_pham;
    }
}
