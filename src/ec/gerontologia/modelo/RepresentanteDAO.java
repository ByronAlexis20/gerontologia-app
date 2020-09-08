package ec.gerontologia.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class RepresentanteDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Representante> getRepresentanteByCedula(String cedula) {
		List<Representante> resultado = new ArrayList<Representante>(); 
		Query query = getEntityManager().createNamedQuery("Representante.buscarPorCedula");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("cedula", cedula);
		resultado = (List<Representante>) query.getResultList();
		return resultado;
	}
}
