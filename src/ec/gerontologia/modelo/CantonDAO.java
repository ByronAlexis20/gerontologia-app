package ec.gerontologia.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class CantonDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Canton> getCantones(Integer idProvincia) {
		List<Canton> resultado = new ArrayList<Canton>(); 
		Query query = getEntityManager().createNamedQuery("Canton.buscarPorProvincia");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idProvincia", idProvincia);
		resultado = (List<Canton>) query.getResultList();
		return resultado;
	}
}
