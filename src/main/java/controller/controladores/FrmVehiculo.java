/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.controladores;

import controller.acceso.VehiculoFacade;
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
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.Vehiculo;

/**
 *
 * @author rafael
 */
@Named(value = "frmVehiculo")
@ViewScoped
public class FrmVehiculo implements Serializable {

    @EJB
    private VehiculoFacade service;
    private Vehiculo selectVehiculo;
    private EstadoCRUD estado;
    private LazyDataModel<Vehiculo> lazyModel;
    private List<Vehiculo> dataSource;

    @PostConstruct
    public void init() {
        this.estado = EstadoCRUD.NUEVO;
        this.lazyModel = new LazyDataModel<Vehiculo>() {
        @Override
            public Object getRowKey(Vehiculo object) {
                if (object != null) {
                    return object.getIdModelo();
                }
                return null;
            }

            @Override
            public Vehiculo getRowData(String rowKey) {
                if (rowKey != null && !rowKey.isEmpty() && this.getWrappedData() != null) {
                    try {
                        Long search = new Long(rowKey);
                        for (Vehiculo tu : (List<Vehiculo>) getWrappedData()) {
                            if (tu.getIdVehiculo().compareTo(search) == 0) {
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
            public List<Vehiculo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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
        this.selectVehiculo = new Vehiculo();
        this.lazyModel.setRowIndex(-1);
    }
    
    public void onRowSelect(SelectEvent event) {
        selectVehiculo = (Vehiculo) event.getObject();
        this.estado = EstadoCRUD.EDITAR;
    }

    public void onRowDeselect(UnselectEvent event) {
        this.estado = EstadoCRUD.NUEVO;
        this.selectVehiculo = new Vehiculo();
        this.lazyModel.setRowIndex(-1);
    }
    
    public void btnCancelarHandler(ActionEvent ae) {
        init();
        this.estado = EstadoCRUD.NUEVO;
    }

    public void btnAgregarHandler(ActionEvent ae) {
        if (selectVehiculo != null) {
            service.create(selectVehiculo);
            init();
        }
    }

    public void btnEditarHandler(ActionEvent ae) {
        if (selectVehiculo != null) {
            service.edit(selectVehiculo);
            init();
        }
    }

    public void btnEliminarHandler(ActionEvent ae) {
        if (selectVehiculo != null) {
            service.remove(selectVehiculo);
            init();
        }
    }

    public LazyDataModel<Vehiculo> getLazyModel() {
        return lazyModel;
    }

    public Vehiculo getSelectVehiculo() {
        if(selectVehiculo == null){
            selectVehiculo = new Vehiculo();
        }
        return selectVehiculo;
    }

    public void setSelectVehiculo(Vehiculo selectVehiculo) {
        this.selectVehiculo = selectVehiculo;
    }

    public EstadoCRUD getEstado() {
        return estado;
    }

    public void setEstado(EstadoCRUD estado) {
        this.estado = estado;
    }
    
    

}
