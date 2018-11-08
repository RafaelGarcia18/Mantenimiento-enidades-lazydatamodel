/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.controladores;

import controller.acceso.ReservaFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.Reserva;

/**
 *
 * @author rafael
 */
@Named(value = "frmReserva")
@ViewScoped
public class FrmReserva implements Serializable {

    @EJB
    private ReservaFacade service;
    private Reserva selectReserva;
    private EstadoCRUD estado;
    private LazyDataModel<Reserva> lazyModel;
    private List<Reserva> dataSource;

    @PostConstruct
    public void init() {
        this.estado = EstadoCRUD.NUEVO;
        this.lazyModel = new LazyDataModel<Reserva>() {
            @Override
            public Object getRowKey(Reserva object) {
                if (object != null) {
                    return object.getIdReserva();
                }
                return null;
            }

            @Override
            public Reserva getRowData(String rowKey) {
                if (rowKey != null && !rowKey.isEmpty() && this.getWrappedData() != null) {
                    try {
                        Long search = new Long(rowKey);
                        for (Reserva tu : (List<Reserva>) getWrappedData()) {
                            if (tu.getIdReserva().compareTo(search) == 0) {
                                return tu;
                            }
                        }
                    } catch (NumberFormatException e) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
                    }
                }
                return null;
            }

            @Override
            public List<Reserva> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                dataSource = new ArrayList<>();
                try {
                    if (service != null) {
                        this.setRowCount(service.count());
                        dataSource = service.findRange(first, pageSize);
                    }
                } catch (Exception e) {
                    System.out.println("Excepcion" + e.getMessage());
                }
                return dataSource;
            }

            @Override
            public int getRowIndex() {
                return super.getRowIndex();
            }
        };
        this.selectReserva = new Reserva();
        this.lazyModel.setRowIndex(-1);
    }
    
    public void onRowSelect(SelectEvent event) {
        selectReserva = (Reserva) event.getObject();
        this.estado = EstadoCRUD.EDITAR;
    }

    public void onRowDeselect(UnselectEvent event) {
        this.estado = EstadoCRUD.NUEVO;
        this.selectReserva = new Reserva();
        this.lazyModel.setRowIndex(-1);
    }

    public void btnCancelarHandler(ActionEvent ae) {
        init();
        this.estado = EstadoCRUD.NUEVO;
    }

    public void btnAgregarHandler(ActionEvent ae) {
        if (selectReserva != null) {
            service.create(selectReserva);
            init();
        }
    }

    public void btnEditarHandler(ActionEvent ae) {
        if (selectReserva != null) {
            service.edit(selectReserva);
            init();
        }
    }

    public void btnEliminarHandler(ActionEvent ae) {
        if (selectReserva != null) {
            service.remove(selectReserva);
            init();
        }
    }

    public LazyDataModel<Reserva> getLazyModel() {
        return lazyModel;
    }

    public Reserva getSelectReserva() {
        if(selectReserva == null){
            selectReserva = new Reserva();
        }
        return selectReserva;
    }

    public void setSelectReserva(Reserva selectReserva) {
        this.selectReserva = selectReserva;
    }

    public EstadoCRUD getEstado() {
        return estado;
    }

    public void setEstado(EstadoCRUD estado) {
        this.estado = estado;
    }

    
}
