/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.controladores;

import controller.acceso.ViajeFacade;
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
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.Viaje;

/**
 *
 * @author rafael
 */
@Named(value = "frmViaje")
@ViewScoped
public class FrmViaje implements Serializable {

    @EJB
    private ViajeFacade service;
    private Viaje selectViaje;
    private EstadoCRUD estado;
    private LazyDataModel<Viaje> lazyModel;
    private List<Viaje> dataSource;

    @PostConstruct
    public void init() {
        this.estado = EstadoCRUD.NUEVO;
        this.lazyModel = new LazyDataModel<Viaje>() {
            @Override
            public Object getRowKey(Viaje object) {
                if (object != null) {
                    return object.getIdReserva();
                }
                return null;
            }

            @Override
            public Viaje getRowData(String rowKey) {
                if (rowKey != null && !rowKey.isEmpty() && this.getWrappedData() != null) {
                    try {
                        Long search = new Long(rowKey);
                        for (Viaje tu : (List<Viaje>) getWrappedData()) {
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
            public List<Viaje> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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
        this.selectViaje = new Viaje();
        this.lazyModel.setRowIndex(-1);
    }

    public void onRowSelect(SelectEvent event) {
        selectViaje = (Viaje) event.getObject();
        this.estado = EstadoCRUD.EDITAR;
    }

    public void onRowDeselect(UnselectEvent event) {
        this.estado = EstadoCRUD.NUEVO;
        this.selectViaje = new Viaje();
        this.lazyModel.setRowIndex(-1);
    }

    public void btnCancelarHandler(ActionEvent ae) {
        init();
        this.estado = EstadoCRUD.NUEVO;
    }

    public void btnAgregarHandler(ActionEvent ae) {
        if (selectViaje != null) {
            service.create(selectViaje);
            init();
        }
    }

    public void btnEditarHandler(ActionEvent ae) {
        if (selectViaje != null) {
            service.edit(selectViaje);
            init();
        }
    }

    public void btnEliminarHandler(ActionEvent ae) {
        if (selectViaje != null) {
            service.remove(selectViaje);
            init();
        }
    }

    public LazyDataModel<Viaje> getLazyModel() {
        return lazyModel;
    }

    public Viaje getSelectViaje() {
        if (selectViaje == null) {
            selectViaje = new Viaje();
        }
        return selectViaje;
    }

    public void setSelectViaje(Viaje selectViaje) {
        this.selectViaje = selectViaje;
    }

    public EstadoCRUD getEstado() {
        return estado;
    }

    public void setEstado(EstadoCRUD estado) {
        this.estado = estado;
    }

}
