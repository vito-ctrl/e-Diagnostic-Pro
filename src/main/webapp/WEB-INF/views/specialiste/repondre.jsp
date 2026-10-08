<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Répondre à l'expertise - Médecin Spécialiste" />
</jsp:include>

<div class="max-w-4xl mx-auto space-y-6">
    <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm flex items-center justify-between">
        <div>
            <h1 class="text-2xl font-bold text-slate-900">US8 : Répondre à une demande de télé-expertise</h1>
            <p class="mt-1 text-sm text-slate-500">
                Formulation de l'avis médical spécialisé et des recommandations thérapeutiques
            </p>
        </div>
        <a href="${pageContext.request.contextPath}/specialiste/expertises" class="text-sm font-medium text-slate-500 hover:text-slate-800">
            <i class="fa-solid fa-arrow-left mr-1"></i> Retour aux demandes
        </a>
    </div>

    <!-- Récapitulatif du cas -->
    <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4">
        <h2 class="text-lg font-bold text-slate-900 border-b border-slate-100 pb-3 flex items-center justify-between">
            <span>Dossier : ${demande.consultation.patient.nomComplet}</span>
            <span class="px-2.5 py-0.5 rounded-full text-xs font-mono bg-slate-100 text-slate-800">
                ${demande.consultation.patient.numSecu}
            </span>
        </h2>
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 text-xs">
            <div class="p-3 bg-slate-50 rounded-xl space-y-1">
                <span class="font-bold text-slate-700 uppercase block">Demandeur :</span>
                <div>${demande.consultation.generaliste.nomComplet} (Généraliste)</div>
                <div>Date : ${demande.dateDemande.toString().substring(0, 16).replace('T', ' ')}</div>
                <div>Priorité : <strong class="text-purple-700">${demande.priorite.libelle}</strong></div>
            </div>
            <div class="p-3 bg-purple-50 rounded-xl border border-purple-100 space-y-1">
                <span class="font-bold text-purple-900 uppercase block">Question posée au spécialiste :</span>
                <p class="text-slate-800 font-medium">${demande.question}</p>
            </div>
        </div>
        <div class="p-3 bg-slate-50 rounded-xl text-xs space-y-1">
            <span class="font-bold text-slate-700 uppercase block">Données cliniques & observations du généraliste :</span>
            <p class="text-slate-700">${demande.consultation.observations}</p>
        </div>
    </div>

    <!-- Formulaire d'avis d'expert -->
    <form action="${pageContext.request.contextPath}/specialiste/repondre" method="POST" class="space-y-6">
        <input type="hidden" name="csrfToken" value="${csrfToken}" />
        <input type="hidden" name="demandeId" value="${demande.id}" />

        <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4">
            <h2 class="text-lg font-bold text-slate-900 border-b border-slate-100 pb-3 flex items-center">
                <i class="fa-solid fa-comment-medical text-purple-600 mr-2"></i> Avis Spécialisé & Recommandations
            </h2>

            <div>
                <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">
                    Avis médical d'expert (Diagnostic, confirmation ou analyse) *
                </label>
                <textarea name="avisMedical" required rows="4" placeholder="Indiquez votre analyse diagnostique détaillée ou interprétation des examens..."
                          class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-purple-500"></textarea>
            </div>

            <div>
                <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">
                    Recommandations thérapeutiques & Conduite à tenir *
                </label>
                <textarea name="recommandations" required rows="3" placeholder="Recommandations sur le protocole de soins, ajustements médicamenteux, examens additionnels..."
                          class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-purple-500"></textarea>
            </div>
        </div>

        <div class="flex justify-end space-x-3">
            <a href="${pageContext.request.contextPath}/specialiste/expertises"
               class="px-5 py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-xl text-sm font-semibold transition">
                Annuler
            </a>
            <button type="submit"
                    class="px-6 py-2.5 bg-purple-600 hover:bg-purple-700 text-white rounded-xl text-sm font-bold shadow-md transition flex items-center space-x-2">
                <i class="fa-solid fa-check"></i>
                <span>Transmettre l'avis et Clôturer l'expertise (Statut : TERMINEE)</span>
            </button>
        </div>
    </form>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
