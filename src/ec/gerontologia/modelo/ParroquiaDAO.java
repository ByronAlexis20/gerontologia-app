package ec.gerontologia.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class ParroquiaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Parroquia> getParroquias(Integer idCanton) {
		List<Parroquia> resultado = new ArrayList<Parroquia>(); 
		Query query = getEntityManager().createNamedQuery("Parroquia.buscarPorCanton");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idCanton", idCanton);
		resultado = (List<Parroquia>) query.getResultList();
		return resultado;
	}
}
