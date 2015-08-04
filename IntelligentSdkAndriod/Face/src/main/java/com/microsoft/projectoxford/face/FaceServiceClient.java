package com.microsoft.projectoxford.face;

import java.io.IOException;
import java.io.InputStream;
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

public interface FaceServiceClient {

    public Face[] detect(String url, boolean analyzesFaceLandmarks, boolean analyzesAge, boolean analyzesGender, boolean analyzesHeadPose) throws FaceServiceException;

    public Face[] detect(InputStream image, boolean analyzesFaceLandmarks, boolean analyzesAge, boolean analyzesGender, boolean analyzesHeadPose) throws FaceServiceException, IOException;

    public VerifyResult verify(UUID faceId1, UUID faceId2) throws FaceServiceException;

    public IdentifyResult[] identity(String personGroupId, UUID[] faceIds, int maxNumOfCandidatesReturned) throws FaceServiceException;

    public PersonGroupTrainingStatus trainPersonGroup(String personGroupId) throws FaceServiceException;

    public PersonGroupTrainingStatus getPersonGroupTrainingStatus(String personGroupId) throws FaceServiceException;

    public void createPersonGroup(String personGroupId, String name, String userData) throws FaceServiceException;

    public void deletePersonGroup(String personGroupId) throws FaceServiceException;

    public void updatePersonGroup(String personGroupId, String name, String userData) throws FaceServiceException;

    public PersonGroup getPersonGroup(String personGroupId) throws FaceServiceException;

    public PersonGroup[] getPersonGroups() throws FaceServiceException;

    public CreatePersonResult createPerson(String personGroupId, UUID[] faceIds, String name, String userData) throws FaceServiceException;

    public Person getPerson(String personGroupId, UUID personId) throws FaceServiceException;

    public Person[] getPersons(String personGroupId) throws FaceServiceException;

    public void addPersonFace(String personGroupId, UUID personId, UUID faceId, String userData) throws FaceServiceException;

    public PersonFace getPersonFace(String personGroupId, UUID personId, UUID faceId) throws FaceServiceException;

    public void updatePersonFace(String personGroupId, UUID personId, UUID faceId, String userData) throws FaceServiceException;

    public void updatePerson(String personGroupId, UUID personId, UUID[] faceIds, String name, String userData) throws FaceServiceException;

    public void deletePerson(String personGroupId, UUID personId) throws FaceServiceException;

    public void deletePersonFace(String personGroupId, UUID personId, UUID faceId) throws FaceServiceException;

    public SimilarFace[] findSimilar(UUID faceId, UUID[] faceIds) throws FaceServiceException;

    public SimilarFace[] findSimilar(UUID faceId, UUID[] faceIds, int maxNumOfCandidatesReturned) throws FaceServiceException;

    public SimilarFace[] findSimilar(UUID faceId, String faceGroupId) throws FaceServiceException;

    public SimilarFace[] findSimilar(UUID faceId, String faceGroupId, int maxNumOfCandidatesReturned) throws FaceServiceException;

    public GroupResult group(UUID[] faceIds) throws FaceServiceException;

    public void createFaceGroup(String faceGroupId, String name, String userData, FaceMetadata[] faces) throws FaceServiceException;

    public FaceGroup getFaceGroup(String faceGroupId) throws FaceServiceException;

    public FaceGroupMetadata[] listFaceGroups() throws FaceServiceException;

    public void updateFaceGroup(String faceGroupId, String name, String userData) throws FaceServiceException;

    public void deleteFaceGroup(String faceGroupId) throws FaceServiceException;

    public void addFacesToFaceGroup(String faceGroupId, FaceMetadata[] faces) throws FaceServiceException;

    public void deleteFacesFromFaceGroup(String faceGroupId, UUID[] faceIds) throws FaceServiceException;
}
