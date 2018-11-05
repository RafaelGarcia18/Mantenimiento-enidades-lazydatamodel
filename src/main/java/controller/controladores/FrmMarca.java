package controller.controladores;

import controller.acceso.MarcaFacade;
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
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.Marca;

/**
 *
 * @author rafael
 */
@Named(value = "frmMarca")
@ViewScoped
public class FrmMarca implements Serializable {

    @EJB
    private MarcaFacade service;
    private LazyDataModel<Marca> lazyModel;
    private Marca selectMarca;
    private EstadoCRUD estado;
    private List<Marca> dataSource;

    @PostConstruct
    public void init() {
        this.estado = EstadoCRUD.NUEVO;
        this.lazyModel = new LazyDataModel<Marca>() {
            @Override
            public List<Marca> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                dataSource = new ArrayList<>();
                try {
                    this.setRowCount(service.count());
                    dataSource = service.findRange(first, pageSize);
                } catch (Exception e) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
                }
                return dataSource;
            }

            @Override
            public int getRowIndex() {
                return super.getRowIndex(); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Object getRowKey(Marca object) {
                if (object != null) {
                    return object.getIdMarca();
                }
                return null;
            }

            @Override
            public Marca getRowData(String rowKey) {
                if (rowKey != null && !rowKey.isEmpty() && this.getWrappedData() != null) {
                    try {
                        Integer search = new Integer(rowKey);
                        for (Marca tu : (List<Marca>) getWrappedData()) {
                            if (tu.getIdMarca().compareTo(search) == 0) {
                                return tu;
                            }
                        }
                    } catch (NumberFormatException e) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
                    }
                }
                return null;
            }

        };
        this.selectMarca = new Marca();
        this.lazyModel.setRowIndex(-1);
    }

    public void btnAgregarHandler(ActionEvent ae) {
        if (selectMarca != null) {
            service.create(selectMarca);
            init();
        }
    }

    public void btnCancelarHandler(ActionEvent ae) {
        init();
        this.estado = EstadoCRUD.NUEVO;
    }

    public void btnEditarHandler(ActionEvent ae) {
        if (selectMarca != null) {
            service.edit(selectMarca);
            init();
        }
    }

    public void btnEliminarHandler(ActionEvent ae) {
        if (selectMarca != null) {
            service.remove(selectMarca);
            init();
        }
    }
    
    public void onRowSelect(SelectEvent event) {
        selectMarca = (Marca) event.getObject();
        this.estado = EstadoCRUD.EDITAR;
    }

    public void onRowDeselect(UnselectEvent event) {
        this.estado = EstadoCRUD.NUEVO;
        this.selectMarca = new Marca();
        this.lazyModel.setRowIndex(-1);
    }

    public LazyDataModel<Marca> getLazyModel() {
        return lazyModel;
    }

    public Marca getSelectMarca() {
        return selectMarca;
    }

    public void setSelectMarca(Marca selectMarca) {
        this.selectMarca = selectMarca;
    }

    public EstadoCRUD getEstado() {
        return estado;
    }

    public void setEstado(EstadoCRUD estado) {
        this.estado = estado;
    }

}
