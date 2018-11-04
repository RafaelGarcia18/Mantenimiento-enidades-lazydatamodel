package controller.controladores;

import controller.acceso.TipoVehiculoFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.TipoUsuario;
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.TipoVehiculo;

/**
 *
 * @author rafael
 */
@Named(value = "frmTipoVehiculo")
@ViewScoped
public class FrmTipoVehiculo implements Serializable {

    @EJB
    private TipoVehiculoFacade service;
    private LazyDataModel<TipoVehiculo> lazymodel;
    private TipoVehiculo selectTipoVehiculo;
    private EstadoCRUD estado;

    @PostConstruct
    public void init() {
        final List<TipoVehiculo> dataSource = service.findAll();
        this.estado = EstadoCRUD.NUEVO;
        this.lazymodel = new AbstractLazyModel(dataSource) {
            @Override
            public Object getRowData(String rowKey) {
                for (TipoVehiculo car : dataSource) {
                    if (car.getIdTipoVehiculo().toString().equals(rowKey)) {
                        return car;
                    }
                }
                return null;
            }

            public Object getRowKey(TipoVehiculo car) {
                return car.getIdTipoVehiculo();
            }

        };
        this.lazymodel.setRowIndex(-1);
    }

    public void onRowSelect(SelectEvent event) {
        selectTipoVehiculo = (TipoVehiculo) event.getObject();
        this.estado = EstadoCRUD.EDITAR;
    }

    public void onRowDeselect(UnselectEvent event) {
        this.estado = EstadoCRUD.NUEVO;
        this.selectTipoVehiculo = new TipoVehiculo();
        this.lazymodel.setRowIndex(-1);
    }

    public void btnCancelarHandler(ActionEvent ae) {
        this.estado = EstadoCRUD.NUEVO;
        if (this.selectTipoVehiculo != null && this.service != null) {
            this.selectTipoVehiculo = new TipoVehiculo();
        }
    }

    public void btnAgregarHandler(ActionEvent ae) {
        if (selectTipoVehiculo != null) {
            service.create(selectTipoVehiculo);
            init();
        }
    }

    public void btnEditarHandler(ActionEvent ae) {
        if(selectTipoVehiculo != null){
            service.edit(selectTipoVehiculo);
            init();
        }
    }
    
    public void btnEliminarHandler(ActionEvent ae) {
        if(selectTipoVehiculo != null){
            service.remove(selectTipoVehiculo);
            init();
        }
    }

    public LazyDataModel<TipoVehiculo> getLazymodel() {
        return lazymodel;
    }

    public TipoVehiculo getSelectTipoVehiculo() {
        if (selectTipoVehiculo == null) {
            selectTipoVehiculo = new TipoVehiculo();
        }
        return selectTipoVehiculo;
    }

    public void setSelectTipoVehiculo(TipoVehiculo selectTipoVehiculo) {
        this.selectTipoVehiculo = selectTipoVehiculo;
    }

    public EstadoCRUD getEstado() {
        return estado;
    }

    public void setEstado(EstadoCRUD estado) {
        this.estado = estado;
    }

}
