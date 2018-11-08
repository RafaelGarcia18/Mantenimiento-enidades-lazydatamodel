/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.controladores;

import controller.acceso.EstadoVehiculoFacade;
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
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.EstadoVehiculo;

/**
 *
 * @author rafael
 */
@Named(value = "frmEstadoVehiculo")
@ViewScoped
public class FrmEstadoVehiculo implements Serializable {

    @EJB
    private EstadoVehiculoFacade service;
    private EstadoCRUD estado;
    private EstadoVehiculo selectEstadoVehiculo;
    private LazyDataModel<EstadoVehiculo> lazyModel;
    private List<EstadoVehiculo> dataSource;

    @PostConstruct
    public void init() {
        this.estado = EstadoCRUD.NUEVO;
        this.lazyModel = new LazyDataModel<EstadoVehiculo>() {
            @Override
            public Object getRowKey(EstadoVehiculo object) {
                if (object != null) {
                    return object.getIdEstadoVehiculo();
                }
                return null;
            }

            @Override
            public EstadoVehiculo getRowData(String rowKey) {
                if (rowKey != null && !rowKey.isEmpty() && this.getWrappedData() != null) {
                    try {
                        Long search = new Long(rowKey);
                        for (EstadoVehiculo tu : (List<EstadoVehiculo>) getWrappedData()) {
                            if (tu.getIdEstadoVehiculo().compareTo(search) == 0) {
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
            public List<EstadoVehiculo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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
        this.lazyModel.setRowIndex(-1);
        this.selectEstadoVehiculo = new EstadoVehiculo();
    }

    public void onRowSelect(SelectEvent event) {
        selectEstadoVehiculo = (EstadoVehiculo) event.getObject();
        this.estado = EstadoCRUD.EDITAR;
    }

    public void onRowDeselect(UnselectEvent event) {
        this.estado = EstadoCRUD.NUEVO;
        this.selectEstadoVehiculo = new EstadoVehiculo();
        this.lazyModel.setRowIndex(-1);
    }

    public void btnCancelarHandler(ActionEvent ae) {
        init();
        this.estado = EstadoCRUD.NUEVO;
    }

    public void btnAgregarHandler(ActionEvent ae) {
        if (selectEstadoVehiculo != null) {
            service.create(selectEstadoVehiculo);
            init();
        }
    }

    public void btnEditarHandler(ActionEvent ae) {
        if (selectEstadoVehiculo != null) {
            service.edit(selectEstadoVehiculo);
            init();
        }
    }

    public void btnEliminarHandler(ActionEvent ae) {
        if (selectEstadoVehiculo != null) {
            service.remove(selectEstadoVehiculo);
            init();
        }
    }

    public LazyDataModel<EstadoVehiculo> getLazyModel() {
        return lazyModel;
    }

    public EstadoCRUD getEstado() {
        return estado;
    }

    public void setEstado(EstadoCRUD estado) {
        this.estado = estado;
    }

    public EstadoVehiculo getSelectEstadoVehiculo() {
        if(selectEstadoVehiculo == null){
            selectEstadoVehiculo = new EstadoVehiculo();
        }
        return selectEstadoVehiculo;
    }

    public void setSelectEstadoVehiculo(EstadoVehiculo selectEstadoVehiculo) {
        this.selectEstadoVehiculo = selectEstadoVehiculo;
    }

}
