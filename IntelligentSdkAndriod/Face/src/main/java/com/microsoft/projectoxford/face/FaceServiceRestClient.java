package com.microsoft.projectoxford.face;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.microsoft.projectoxford.face.contract.CreatePersonResult;
import com.microsoft.projectoxford.face.contract.FaceGroup;
import com.microsoft.projectoxford.face.contract.FaceGroupMetadata;
import com.microsoft.projectoxford.face.contract.FaceMetadata;
import com.microsoft.projectoxford.face.contract.GroupResult;
import com.microsoft.projectoxford.face.contract.PersonGroup;
import com.microsoft.projectoxford.face.contract.PersonGroupTrainingStatus;
import com.microsoft.projectoxford.face.contract.SimilarFace;
import com.microsoft.projectoxford.face.rest.FaceServiceException;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.IdentifyResult;
import com.microsoft.projectoxford.face.contract.PersonFace;
import com.microsoft.projectoxford.face.contract.VerifyResult;
import com.microsoft.projectoxford.face.contract.Person;
import com.microsoft.projectoxford.face.rest.WebServiceRequest;

public class FaceServiceRestClient implements FaceServiceClient {
    private static final String serviceHost = "https://api.projectoxford.ai/face/v0";
    private WebServiceRequest restCall = null;
    private Gson gson = new Gson();

    public FaceServiceRestClient(String subscriptKey) {
        this.restCall = new WebServiceRequest(subscriptKey);
    }

    @Override
    public Face[] detect(String url, boolean analyzesFaceLandmarks, boolean analyzesAge, boolean analyzesGender, boolean analyzesHeadPose) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();

        params.put("analyzesFaceLandmarks", analyzesFaceLandmarks);
        params.put("analyzesAge", analyzesAge);
        params.put("analyzesGender", analyzesGender);
        params.put("analyzesHeadPose", analyzesHeadPose);

        String path = serviceHost + "/detections";
        String uri = WebServiceRequest.getUrl(path, params);

        params.clear();
        params.put("url", url);

        String json = (String)this.restCall.request(uri, "POST", params, null);
        Type listType = new TypeToken<List<Face>>() {
        }.getType();
        List<Face> faces = this.gson.fromJson(json, listType);

        return faces.toArray(new Face[faces.size()]);
    }

    @Override
    public Face[] detect(InputStream image, boolean analyzesFaceLandmarks, boolean analyzesAge, boolean analyzesGender, boolean analyzesHeadPose) throws FaceServiceException, IOException {
        Map<String, Object> params = new HashMap<>();

        params.put("analyzesAge", analyzesAge);
        params.put("analyzesGender", analyzesGender);
        params.put("analyzesFaceLandmarks", analyzesFaceLandmarks);
        params.put("analyzesHeadPose", analyzesHeadPose);

        String path = serviceHost + "/detections";
        String uri = WebServiceRequest.getUrl(path, params);

        byte[] data = IOUtils.toByteArray(image);
        params.clear();
        params.put("data", data);

        String json = (String)this.restCall.request(uri, "POST", params, "application/octet-stream");
        Type listType = new TypeToken<List<Face>>() {
        }.getType();
        List<Face> faces = this.gson.fromJson(json, listType);

        return faces.toArray(new Face[faces.size()]);
    }

    @Override
    public VerifyResult verify(UUID faceId1, UUID faceId2) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();

        params.put("faceId1", faceId1);
        params.put("faceId2", faceId2);

        String uri = serviceHost + "/verifications";
        String json = (String)this.restCall.request(uri, "POST", params, null);
        return this.gson.fromJson(json, VerifyResult.class);
    }

    @Override
    public IdentifyResult[] identity(String personGroupId, UUID[] faceIds, int maxNumOfCandidatesReturned) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();

        params.put("personGroupId", personGroupId);
        params.put("faceIds", faceIds);
        params.put("maxNumOfCandidatesReturned", maxNumOfCandidatesReturned);

        String uri = serviceHost + "/identifications";
        String json = (String)this.restCall.request(uri, "POST", params, null);
        Type listType = new TypeToken<List<VerifyResult>>() {
        }.getType();
        List<IdentifyResult> result = this.gson.fromJson(json, listType);

        return result.toArray(new IdentifyResult[result.size()]);
    }

    @Override
    public PersonGroupTrainingStatus trainPersonGroup(String personGroupId) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/persongroups/" + personGroupId + "/training";
        String json = (String)this.restCall.request(uri, "POST", params, null);
        return this.gson.fromJson(json, PersonGroupTrainingStatus.class);
    }

    @Override
    public PersonGroupTrainingStatus getPersonGroupTrainingStatus(String personGroupId) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/persongroups/" + personGroupId + "/training";
        String json = (String)this.restCall.request(uri, "GET", params, null);
        return this.gson.fromJson(json, PersonGroupTrainingStatus.class);
    }

    @Override
    public void createPersonGroup(String personGroupId, String name, String userData) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/persongroups/" + personGroupId;
        params.put("name", name);
        params.put("userData", userData);
        this.restCall.request(uri, "PUT", params, null);
    }

    @Override
    public void deletePersonGroup(String personGroupId) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/persongroups/" + personGroupId;
        this.restCall.request(uri, "DELETE", params, null);
    }

    @Override
    public void updatePersonGroup(String personGroupId, String name, String userData) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/persongroups/" + personGroupId;
        if(name != null) {
            params.put("name", name);
        }

        if (userData != null) {
            params.put("userData", userData);
        }

        this.restCall.request(uri, "PATCH", params, null);
    }

    @Override
    public PersonGroup getPersonGroup(String personGroupId) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/persongroups/" + personGroupId;
        String json = (String)this.restCall.request(uri, "GET", params, null);
        return gson.fromJson(json, PersonGroup.class);
    }

    @Override
    public PersonGroup[] getPersonGroups() throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/persongroups";
        String json = (String)this.restCall.request(uri, "GET", params, null);

        Type listType = new TypeToken<List<PersonGroup>>() {
        }.getType();
        List<PersonGroup> result = this.gson.fromJson(json, listType);

        return result.toArray(new PersonGroup[result.size()]);
    }

    @Override
    public CreatePersonResult createPerson(String personGroupId, UUID[] faceIds, String name, String userData) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();

        String uri = serviceHost + "/persongroups/" + personGroupId + "/persons";
        params.put("faceIds", faceIds);
        params.put("name", name);
        params.put("userData", userData);

        String json = (String)this.restCall.request(uri, "POST", params, null);
        return this.gson.fromJson(json, CreatePersonResult.class);
    }

    @Override
    public Person getPerson(String personGroupId, UUID personId) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();

        String uri = serviceHost + "/persongroups/" + personGroupId + "/persons/" + personId.toString();
        String json = (String)this.restCall.request(uri, "GET", params, null);
        return this.gson.fromJson(json, Person.class);
    }

    @Override
    public Person[] getPersons(String personGroupId) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();

        String uri = serviceHost + "/persongroups/" + personGroupId + "/persons";
        String json = (String)this.restCall.request(uri, "GET", params, null);
        Type listType = new TypeToken<List<Person>>() {
        }.getType();
        List<Person> result = this.gson.fromJson(json, listType);
        return result.toArray(new Person[result.size()]);
    }

    @Override
    public void addPersonFace(String personGroupId, UUID personId, UUID faceId, String userData) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();

        String uri = serviceHost + "/persongroups/" + personGroupId + "/persons/" + personId + "/faces/" + faceId;
        params.put("faceIds", userData);

        this.restCall.request(uri, "PUT", params, null);
    }

    @Override
    public PersonFace getPersonFace(String personGroupId, UUID personId, UUID faceId) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/persongroups/" + personGroupId + "/persons/" + personId + "/faces/" + faceId;
        String json = (String)this.restCall.request(uri, "GET", params, null);
        return this.gson.fromJson(json, PersonFace.class);
    }

    @Override
    public void updatePersonFace(String personGroupId, UUID personId, UUID faceId, String userData) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        if (userData != null) {
            params.put("userData", userData);
        }

        String uri = serviceHost + "/persongroups/" + personGroupId + "/persons/" + personId + "/faces/" + faceId;
        this.restCall.request(uri, "PATCH", params, null);
    }

    @Override
    public void updatePerson(String personGroupId, UUID personId, UUID[] faceIds, String name, String userData) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        if(name != null) {
            params.put("name", name);
        }

        if (userData != null) {
            params.put("userData", userData);
        }

        String uri = serviceHost + "/persongroups/" + personGroupId + "/persons/" + personId;
        params.put("faceIds", faceIds);
        this.restCall.request(uri, "PATCH", params, null);
    }

    @Override
    public void deletePerson(String personGroupId, UUID personId) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/persongroups/" + personGroupId + "/persons/" + personId;
        this.restCall.request(uri, "DELETE", params, null);
    }

    @Override
    public void deletePersonFace(String personGroupId, UUID personId, UUID faceId) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/persongroups/" + personGroupId + "/persons/" + personId + "/faces/" + faceId;
        this.restCall.request(uri, "DELETE", params, null);
    }

    @Override
    public SimilarFace[] findSimilar(UUID faceId, UUID[] faceIds) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/findsimilars";
        params.put("faceId", faceId);
        params.put("faceIds", faceIds);
        String json = (String)this.restCall.request(uri, "POST", params, null);
        Type listType = new TypeToken<List<SimilarFace>>() {
        }.getType();
        List<SimilarFace> result = this.gson.fromJson(json, listType);
        return result.toArray(new SimilarFace[result.size()]);
    }

    @Override
    public SimilarFace[] findSimilar(UUID faceId, UUID[] faceIds, int maxNumOfCandidatesReturned) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/findsimilars";
        params.put("faceId", faceId);
        params.put("faceIds", faceIds);
        params.put("maxNumOfCandidatesReturned", maxNumOfCandidatesReturned);
        String json = (String)this.restCall.request(uri, "POST", params, null);
        Type listType = new TypeToken<List<SimilarFace>>() {
        }.getType();
        List<SimilarFace> result = this.gson.fromJson(json, listType);
        return result.toArray(new SimilarFace[result.size()]);
    }

    @Override
    public SimilarFace[] findSimilar(UUID faceId, String faceGroupId) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/findsimilars";
        params.put("faceId", faceId);
        params.put("faceGroupId", faceGroupId);
        String json = (String)this.restCall.request(uri, "POST", params, null);
        Type listType = new TypeToken<List<SimilarFace>>() {
        }.getType();
        List<SimilarFace> result = this.gson.fromJson(json, listType);
        return result.toArray(new SimilarFace[result.size()]);
    }

    @Override
    public SimilarFace[] findSimilar(UUID faceId, String faceGroupId, int maxNumOfCandidatesReturned) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/findsimilars";
        params.put("faceId", faceId);
        params.put("faceGroupId", faceGroupId);
        params.put("maxNumOfCandidatesReturned", maxNumOfCandidatesReturned);
        String json = (String)this.restCall.request(uri, "POST", params, null);
        Type listType = new TypeToken<List<SimilarFace>>() {
        }.getType();
        List<SimilarFace> result = this.gson.fromJson(json, listType);
        return result.toArray(new SimilarFace[result.size()]);
    }

    @Override
    public GroupResult group(UUID[] faceIds) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/groupings";
        params.put("faceIds", faceIds);
        String json = (String)this.restCall.request(uri, "POST", params, null);
        return this.gson.fromJson(json, GroupResult.class);
    }

    @Override
    public void createFaceGroup(String faceGroupId, String name, String userData, FaceMetadata[] faces) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/facegroups/" + faceGroupId;
        params.put("name", name);
        params.put("userData", userData);
        params.put("faces", faces);
        this.restCall.request(uri, "PUT", params, null);
    }

    @Override
    public FaceGroup getFaceGroup(String faceGroupId) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/facegroups/" + faceGroupId;
        String json = (String)this.restCall.request(uri, "GET", params, null);
        return this.gson.fromJson(json, FaceGroup.class);
    }

    @Override
    public FaceGroupMetadata[] listFaceGroups() throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/facegroups";
        String json = (String)this.restCall.request(uri, "GET", params, null);
        Type listType = new TypeToken<List<FaceGroupMetadata>>() {
        }.getType();
        List<FaceGroupMetadata> result = this.gson.fromJson(json, listType);
        return result.toArray(new FaceGroupMetadata[result.size()]);
    }

    @Override
    public void updateFaceGroup(String faceGroupId, String name, String userData) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        if(name != null) {
            params.put("name", name);
        }

        if (userData != null) {
            params.put("userData", userData);
        }

        String uri = serviceHost + "/facegroups/" + faceGroupId;
        this.restCall.request(uri, "PATCH", params, null);
    }

    @Override
    public void deleteFaceGroup(String faceGroupId) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/facegroups/" + faceGroupId;
        this.restCall.request(uri, "DELETE", params, null);
    }

    @Override
    public void addFacesToFaceGroup(String faceGroupId, FaceMetadata[] faces) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/facegroups/" + faceGroupId + "/faces";
        params.put("faces", faces);
        this.restCall.request(uri, "PUT", params, null);
    }

    @Override
    public void deleteFacesFromFaceGroup(String faceGroupId, UUID[] faceIds) throws FaceServiceException {
        Map<String, Object> params = new HashMap<>();
        String uri = serviceHost + "/facegroups/" + faceGroupId + "/faces";
        params.put("faceIds", faceIds);
        this.restCall.request(uri, "DELETE", params, null);
    }
}
