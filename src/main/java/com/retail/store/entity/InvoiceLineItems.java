package com.retail.store.entity;

import static javax.persistence.FetchType.EAGER;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invoice_line_items")
@NoArgsConstructor
public class InvoiceLineItems extends BaseEntity {

	@JsonBackReference
    @ManyToOne
    @JoinColumn(name = "invoice_id_fk")
    private Invoice invoice;

    @OneToOne(fetch = EAGER)
    @JoinColumn(name = "product_id_fk")
    private Product product;

    @Column(name = "product_qty")
    private Integer productQty;

    @Column(name = "product_price")
    private Integer productPrice;
    
}
