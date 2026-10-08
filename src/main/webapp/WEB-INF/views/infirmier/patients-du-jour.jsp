<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Patients du jour - Infirmier" />
</jsp:include>

<div class="space-y-6">
    <!-- Header with Action -->
    <div class="sm:flex sm:items-center sm:justify-between bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
        <div>
            <h1 class="text-2xl font-bold text-slate-900">US2 : Patients enregistrés du jour</h1>
            <p class="mt-1 text-sm text-slate-500">
                Liste ordonnée par heure d'arrivée (du plus ancien au plus récent) &bull; Filtrée via Java Stream API
            </p>
        </div>
        <div class="mt-4 sm:mt-0 flex items-center space-x-3">
            <form action="${pageContext.request.contextPath}/infirmier/patients" method="GET" class="flex items-center space-x-2">
                <input type="date" name="date" value="${selectedDate}"
                       class="px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" />
                <button type="submit" class="px-3 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-xl text-sm font-medium">
                    <i class="fa-solid fa-filter mr-1"></i> Filtrer
                </button>
            </form>
            <a href="${pageContext.request.contextPath}/infirmier/recherche"
               class="px-4 py-2 bg-emerald-600 hover:bg-emerald-700 text-white rounded-xl text-sm font-semibold shadow-sm flex items-center space-x-1.5 transition">
                <i class="fa-solid fa-user-plus"></i>
                <span>Accueillir un patient</span>
            </a>
        </div>
    </div>

    <c:if test="${param.success == '1'}">
        <div class="p-4 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-800 text-sm flex items-center space-x-2">
            <i class="fa-solid fa-circle-check text-emerald-600 text-lg"></i>
            <span>Le patient a été enregistré et ajouté avec succès à la file d'attente !</span>
        </div>
    </c:if>

    <!-- Patients List Table -->
    <div class="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
        <div class="overflow-x-auto">
            <table class="min-w-full divide-y divide-slate-200 text-left text-sm">
                <thead class="bg-slate-50 text-slate-500 uppercase font-semibold text-xs tracking-wider">
                    <tr>
                        <th class="px-6 py-4">Heure d'arrivée</th>
                        <th class="px-6 py-4">Patient</th>
                        <th class="px-6 py-4">N° Sécurité Sociale</th>
                        <th class="px-6 py-4">Signes Vitaux</th>
                        <th class="px-6 py-4">Statut File</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-slate-100 bg-white">
                    <c:choose>
                        <c:when test="${empty patientsDuJour}">
                            <tr>
                                <td colspan="5" class="px-6 py-12 text-center text-slate-400">
                                    <i class="fa-solid fa-clipboard-user text-4xl mb-3 text-slate-300"></i>
                                    <p class="font-medium text-slate-600">Aucun patient enregistré pour cette date.</p>
                                    <p class="text-xs text-slate-400 mt-1">Utilisez le bouton "Accueillir un patient" pour enregistrer une arrivée.</p>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="item" items="${patientsDuJour}">
                                <tr class="hover:bg-slate-50/80 transition">
                                    <td class="px-6 py-4 whitespace-nowrap font-medium text-slate-900">
                                        <div class="flex items-center space-x-2">
                                            <i class="fa-regular fa-clock text-sky-600"></i>
                                            <span>${item.heureArrivee.toLocalTime().toString().substring(0, 5)}</span>
                                        </div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <div class="font-semibold text-slate-900">${item.patient.nomComplet}</div>
                                        <div class="text-xs text-slate-500">Né(e) le : ${item.patient.dateNaissance != null ? item.patient.dateNaissance : '-'}</div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-mono font-medium bg-slate-100 text-slate-800 border border-slate-200">
                                            ${item.patient.numSecu}
                                        </span>
                                    </td>
                                    <td class="px-6 py-4">
                                        <c:choose>
                                            <c:when test="${not empty item.signesVitaux}">
                                                <div class="text-xs space-y-1">
                                                    <div>
                                                        <span class="font-semibold text-slate-700">TA:</span> ${item.signesVitaux.tensionArterielle} mmHg &bull;
                                                        <span class="font-semibold text-slate-700">FC:</span> ${item.signesVitaux.frequenceCardiaque} bpm
                                                    </div>
                                                    <div class="text-slate-500">
                                                        <span class="font-semibold text-slate-700">T°:</span> ${item.signesVitaux.temperature} °C &bull;
                                                        <span class="font-semibold text-slate-700">FR:</span> ${item.signesVitaux.frequenceRespiratoire} cpm
                                                        <c:if test="${item.signesVitaux.poids != null}">
                                                            &bull; ${item.signesVitaux.poids} kg
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-xs text-slate-400 italic">Non mesurés</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <span class="px-2.5 py-1 text-xs rounded-full font-semibold
                                            <c:choose>
                                                <c:when test="${item.statut == 'EN_ATTENTE'}">bg-amber-100 text-amber-800 border border-amber-200</c:when>
                                                <c:when test="${item.statut == 'EN_CONSULTATION'}">bg-sky-100 text-sky-800 border border-sky-200</c:when>
                                                <c:otherwise>bg-emerald-100 text-emerald-800 border border-emerald-200</c:otherwise>
                                            </c:choose>">
                                            ${item.statut.libelle}
                                        </span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
