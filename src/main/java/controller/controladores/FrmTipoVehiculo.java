package controller.controladores;

import controller.acceso.TipoVehiculoFacade;
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
    private List<TipoVehiculo> dataSource ;

    @PostConstruct
    public void init() {
        
        this.estado = EstadoCRUD.NUEVO;
        this.lazymodel = new LazyDataModel<TipoVehiculo>() {
            @Override
                public Object getRowKey(TipoVehiculo object) {
                    if (object != null) {
                        return object.getIdTipoVehiculo();
                    }
                    return null;
                }

                @Override
                public TipoVehiculo getRowData(String rowKey) {
                    if (rowKey != null && !rowKey.isEmpty() && this.getWrappedData() != null) {
                        try {
                            Integer search = new Integer(rowKey);
                            for (TipoVehiculo tu : (List<TipoVehiculo>) getWrappedData()) {
                                if (tu.getIdTipoVehiculo().compareTo(search) == 0) {
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
                public List<TipoVehiculo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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
        this.selectTipoVehiculo = new TipoVehiculo();
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
        init();
        this.estado = EstadoCRUD.NUEVO;
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
