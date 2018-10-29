/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.acceso;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.Parte;

/**
 *
 * @author rafael
 */
@Stateless
public class ParteFacade extends AbstractFacade<Parte> {

    @PersistenceContext(unitName = "flota-unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParteFacade() {
        super(Parte.class);
    }
    
}
