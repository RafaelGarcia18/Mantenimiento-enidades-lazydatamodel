package controller.controladores;

import controller.acceso.MarcaFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.LazyDataModel;
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

    @PostConstruct
    public void init() {
        final List<Marca> dataSource = service.findAll();
        this.lazyModel = new AbstractLazyModel(dataSource) {
            @Override
            public Object getRowData(String rowKey) {
                for (Marca car : dataSource) {
                    if (car.getIdMarca().toString().equals(rowKey)) {
                        return car;
                    }
                }
                return null;
            }

            public Object getRowKey(Marca car) {
                return car.getIdMarca();
            }

        };
    }

    public void onRowSelected() {
        System.out.println("Row selected");
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

}
