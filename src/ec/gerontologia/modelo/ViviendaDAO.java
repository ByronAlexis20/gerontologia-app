package ec.gerontologia.modelo;

import java.util.List;

import javax.persistence.Query;

public class ViviendaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Vivienda> getViviendas() {
		List<Vivienda> resultado; 
		Query query = getEntityManager().createNamedQuery("Vivienda.buscarViviendas");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Vivienda>) query.getResultList();
		return resultado;
	}
}
