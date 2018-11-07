/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.controladores;

import controller.acceso.TipoEstadoReservaFacade;
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
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.TipoEstadoReserva;

/**
 *
 * @author rafael
 */
@Named(value = "frmTipoEstadoReserva")
@ViewScoped
public class FrmTipoEstadoReserva implements Serializable {

    @EJB
    private TipoEstadoReservaFacade service;
    private LazyDataModel<TipoEstadoReserva> lazyModel;
    private TipoEstadoReserva selectTEstadoReserva;
    private EstadoCRUD estado;
    private List<TipoEstadoReserva> dataSource;

    @PostConstruct
    public void init() {
        this.estado = EstadoCRUD.NUEVO;
        this.lazyModel = new LazyDataModel<TipoEstadoReserva>() {
            @Override
            public Object getRowKey(TipoEstadoReserva object) {
                if (object != null) {
                    return object.getIdTipoEstadoReserva();
                }
                return null;
            }

            @Override
            public TipoEstadoReserva getRowData(String rowKey) {
                if (rowKey != null && !rowKey.isEmpty() && this.getWrappedData() != null) {
                    try {
                        Integer search = new Integer(rowKey);
                        for (TipoEstadoReserva tu : (List<TipoEstadoReserva>) getWrappedData()) {
                            if (tu.getIdTipoEstadoReserva().compareTo(search) == 0) {
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
            public List<TipoEstadoReserva> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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
        this.selectTEstadoReserva = new TipoEstadoReserva();
        this.lazyModel.setRowIndex(-1);
    }

    public void onRowSelect(SelectEvent event) {
        selectTEstadoReserva = (TipoEstadoReserva) event.getObject();
        this.estado = EstadoCRUD.EDITAR;
    }

    public void onRowDeselect(UnselectEvent event) {
        this.estado = EstadoCRUD.NUEVO;
        this.selectTEstadoReserva = new TipoEstadoReserva();
        this.lazyModel.setRowIndex(-1);
    }

    public void btnCancelarHandler(ActionEvent ae) {
        init();
        this.estado = EstadoCRUD.NUEVO;
    }

    public void btnAgregarHandler(ActionEvent ae) {
        if (selectTEstadoReserva != null) {
            service.create(selectTEstadoReserva);
            init();
        }
    }

    public void btnEditarHandler(ActionEvent ae) {
        if (selectTEstadoReserva != null) {
            service.edit(selectTEstadoReserva);
            init();
        }
    }

    public void btnEliminarHandler(ActionEvent ae) {
        if (selectTEstadoReserva != null) {
            service.remove(selectTEstadoReserva);
            init();
        }
    }

    public LazyDataModel<TipoEstadoReserva> getLazyModel() {
        return lazyModel;
    }

    public TipoEstadoReserva getSelectTEstadoReserva() {
        if (selectTEstadoReserva == null) {
            selectTEstadoReserva = new TipoEstadoReserva();
        }
        return selectTEstadoReserva;
    }

    public void setSelectTEstadoReserva(TipoEstadoReserva selectTEstadoReserva) {
        this.selectTEstadoReserva = selectTEstadoReserva;
    }

    public EstadoCRUD getEstado() {
        return estado;
    }

    public void setEstado(EstadoCRUD estado) {
        this.estado = estado;
    }

}
