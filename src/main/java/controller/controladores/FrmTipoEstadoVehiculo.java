/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.controladores;

import controller.acceso.TipoEstadoVehiculoFacade;
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
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.TipoEstadoVehiculo;

/**
 *
 * @author rafael
 */
@Named(value = "frmTipoEstadoVehiculo")
@ViewScoped
public class FrmTipoEstadoVehiculo implements Serializable {

    @EJB
    private TipoEstadoVehiculoFacade service;
    private LazyDataModel<TipoEstadoVehiculo> lazyModel;
    private TipoEstadoVehiculo selectTEstadoVehiculo;
    private EstadoCRUD estado;
    private List<TipoEstadoVehiculo> dataSource;

    @PostConstruct
    public void init() {
        this.estado = EstadoCRUD.NUEVO;
        this.lazyModel = new LazyDataModel<TipoEstadoVehiculo>() {
            @Override
            public List<TipoEstadoVehiculo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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

            @Override
            public TipoEstadoVehiculo getRowData(String rowKey) {
                if (rowKey != null && !rowKey.isEmpty() && this.getWrappedData() != null) {
                    try {
                        Integer search = new Integer(rowKey);
                        for (TipoEstadoVehiculo tu : (List<TipoEstadoVehiculo>) getWrappedData()) {
                            if (tu.getIdTipoEstadoVehiculo().compareTo(search) == 0) {
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
            public Object getRowKey(TipoEstadoVehiculo object) {
                if (object != null) {
                    return object.getIdTipoEstadoVehiculo();
                }
                return null;
            }
        };
        this.selectTEstadoVehiculo = new TipoEstadoVehiculo();
        this.lazyModel.setRowIndex(-1);
    }

    public void onRowSelect(SelectEvent event) {
        selectTEstadoVehiculo = (TipoEstadoVehiculo) event.getObject();
        this.estado = EstadoCRUD.EDITAR;
    }

    public void onRowDeselect(UnselectEvent event) {
        this.estado = EstadoCRUD.NUEVO;
        this.selectTEstadoVehiculo = new TipoEstadoVehiculo();
        this.lazyModel.setRowIndex(-1);
    }

    public void btnCancelarHandler(ActionEvent ae) {
        init();
        this.estado = EstadoCRUD.NUEVO;
    }

    public void btnAgregarHandler(ActionEvent ae) {
        if (selectTEstadoVehiculo != null) {
            service.create(selectTEstadoVehiculo);
            init();
        }
    }

    public void btnEditarHandler(ActionEvent ae) {
        if (selectTEstadoVehiculo != null) {
            service.edit(selectTEstadoVehiculo);
            init();
        }
    }

    public void btnEliminarHandler(ActionEvent ae) {
        if (selectTEstadoVehiculo != null) {
            service.remove(selectTEstadoVehiculo);
            init();
        }
    }

    public LazyDataModel<TipoEstadoVehiculo> getLazyModel() {
        return lazyModel;
    }

    public TipoEstadoVehiculo getSelectTEstadoVehiculo() {
        if (selectTEstadoVehiculo == null) {
            selectTEstadoVehiculo = new TipoEstadoVehiculo();
        }
        return selectTEstadoVehiculo;
    }

    public void setSelectTEstadoVehiculo(TipoEstadoVehiculo selectTEstadoVehiculo) {
        this.selectTEstadoVehiculo = selectTEstadoVehiculo;
    }

    public EstadoCRUD getEstado() {
        return estado;
    }

    public void setEstado(EstadoCRUD estado) {
        this.estado = estado;
    }

}
