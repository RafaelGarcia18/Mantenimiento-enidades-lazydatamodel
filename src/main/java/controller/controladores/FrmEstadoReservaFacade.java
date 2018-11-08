/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.controladores;

import controller.acceso.EstadoReservaFacade;
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
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.EstadoReserva;

/**
 *
 * @author rafael
 */
@Named(value = "frmEstadoReservaFacade")
@ViewScoped
public class FrmEstadoReservaFacade implements Serializable {

    @EJB
    private EstadoReservaFacade service;
    private EstadoCRUD estado;
    private EstadoReserva selectEstadoReserva;
    private LazyDataModel<EstadoReserva> lazyModel;
    private List<EstadoReserva> dataSource;

    @PostConstruct
    private void init() {
        this.estado = EstadoCRUD.NUEVO;
        this.lazyModel = new LazyDataModel<EstadoReserva>() {
            @Override
            public Object getRowKey(EstadoReserva object) {
                if (object != null) {
                    return object.getIdReserva();
                }
                return null;
            }

            @Override
            public EstadoReserva getRowData(String rowKey) {
                if (rowKey != null && !rowKey.isEmpty() && this.getWrappedData() != null) {
                    try {
                        Long search = new Long(rowKey);
                        for (EstadoReserva tu : (List<EstadoReserva>) getWrappedData()) {
                            if (tu.getIdEstadoReserva().compareTo(search) == 0) {
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
            public List<EstadoReserva> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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
        this.selectEstadoReserva = new EstadoReserva();
        this.lazyModel.setRowIndex(-1);
    }

    public void onRowSelect(SelectEvent event) {
        selectEstadoReserva = (EstadoReserva) event.getObject();
        this.estado = EstadoCRUD.EDITAR;
    }

    public void onRowDeselect(UnselectEvent event) {
        this.estado = EstadoCRUD.NUEVO;
        this.selectEstadoReserva = new EstadoReserva();
        this.lazyModel.setRowIndex(-1);
    }

    public void btnCancelarHandler(ActionEvent ae) {
        init();
        this.estado = EstadoCRUD.NUEVO;
    }

    public void btnAgregarHandler(ActionEvent ae) {
        if (selectEstadoReserva != null) {
            service.create(selectEstadoReserva);
            init();
        }
    }

    public void btnEditarHandler(ActionEvent ae) {
        if (selectEstadoReserva != null) {
            service.edit(selectEstadoReserva);
            init();
        }
    }

    public void btnEliminarHandler(ActionEvent ae) {
        if (selectEstadoReserva != null) {
            service.remove(selectEstadoReserva);
            init();
        }
    }

    public LazyDataModel<EstadoReserva> getLazyModel() {
        return lazyModel;
    }

    public EstadoCRUD getEstado() {
        return estado;
    }

    public void setEstado(EstadoCRUD estado) {
        this.estado = estado;
    }

    public EstadoReserva getSelectEstadoReserva() {
        if(selectEstadoReserva == null){
            selectEstadoReserva = new EstadoReserva();
        }
        return selectEstadoReserva;
    }

    public void setSelectEstadoReserva(EstadoReserva selectEstadoReserva) {
        this.selectEstadoReserva = selectEstadoReserva;
    }
    
    

}
