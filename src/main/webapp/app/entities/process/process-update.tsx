import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import {  Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICandidate } from 'app/shared/model/candidate.model';
import { getEntities as getCandidates } from 'app/entities/candidate/candidate.reducer';
import { getEntity, updateEntity, createEntity, reset } from './process.reducer';
import { IProcess } from 'app/shared/model/process.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import Select from "react-select";
import value from "*.json";
import {doc} from "prettier";
import label = doc.builders.label;
import {json} from "stream/consumers";
import {values} from "lodash";


export const ProcessUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const candidates = useAppSelector(state => state.candidate.entities);
  const processEntity = useAppSelector(state => state.process.entity);
  const loading = useAppSelector(state => state.process.loading);
  const updating = useAppSelector(state => state.process.updating);
  const updateSuccess = useAppSelector(state => state.process.updateSuccess);
  const account = useAppSelector(state => state.authentication.account);

  const  deger =[

  ];

const gelendegerler= [
  { value: "spring Framework", label: "spring framework" },
];


  const options = [
    { value: "spring framework", label: "spring framework" },
    { value: "react", label: "React" },
    { value: "java", label: "Java" },
    { value: "c#", label: "C#" },
    { value: "Sql", label: "Sql" },
    { value: "fortran", label: "Fortran" },
    { value: "cobol", label: "Cobol" },
    { value: "Algol", label: "Algol" }
  ];

  const [selectedValue, setSelectedValue] = useState([]);

  const handleChange = (e) => {
    setSelectedValue(e);
  }

   const pushladegere =()=>{
    selectedValue.map((sc) => (

       deger.push(sc.value)

   ))
 }
  const gelendegerleriAyır =(c)=>{


    const  ayrıldı =c.split(",");

    ayrıldı.map(ay =>gelendegerler.push({label: ay,value: ay}))



  }



  const handleClose = () => {
    props.history.push('/process');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));

    }

    dispatch(getCandidates({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.pdate = convertDateTimeToServer(values.pdate);

    const entity = {
      ...processEntity,
      ...values,
      candidate: candidates.find(it => it.id.toString() === values.candidate.toString()),

    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          pdate: displayDefaultDateTime(),
          loginu:account.login,

        }
      : {
          ...processEntity,
          pdate: convertDateTimeFromServer(processEntity.pdate),
          candidate: processEntity?.candidate?.id,



        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="iKaMetApp.process.home.createOrEditLabel" data-cy="ProcessCreateUpdateHeading">
            <Translate contentKey="iKaMetApp.process.home.createOrEditLabel">Create or edit a Process</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="process-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('iKaMetApp.process.pdate')}
                id="process-pdate"
                name="pdate"
                data-cy="pdate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('iKaMetApp.process.department')}
                id="process-department"
                name="department"
                data-cy="department"
                type="text"
              />
              <div >

                <div>{processEntity.technicalIndicators}</div>

                <h1>Aday teknik bilgileri </h1>
                {gelendegerleriAyır("ytuuuytu,kkl") }
                <div style={{width :'1000px'}}>
                  <Select
                    placeholder="Select technicalIndicators"
                    closeMenuOnSelect={false}


                   defaultValue={[...gelendegerler]}
                    isMulti
                    options={options}
                    className={"Select"}
                    classNamePrefix={"Select"}
                    onChange={handleChange}
                  />
                </div>
                {pushladegere}


              </div>
              <ValidatedField
                label={translate('iKaMetApp.process.technicalIndicators')}
                id="process-technicalIndicators"
                name="technicalIndicators"
                data-cy="technicalIndicators"
                type="text"
              />
              <ValidatedField
                label={translate('iKaMetApp.process.experience')}
                id="process-experience"
                name="experience"
                data-cy="experience"
                type="text"
              />
              <ValidatedField
                label={translate('iKaMetApp.process.position')}
                id="process-position"
                name="position"
                data-cy="position"
                type="text"
              />
              <ValidatedField
                label={translate('iKaMetApp.process.salaryExpectation')}
                id="process-salaryExpectation"
                name="salaryExpectation"
                data-cy="salaryExpectation"
                type="text"
              />
              <ValidatedField
                label={translate('iKaMetApp.process.possibleAssignmnet')}
                id="process-possibleAssignmnet"
                name="possibleAssignmnet"
                data-cy="possibleAssignmnet"
                type="text"
              />
              <ValidatedField
                label={translate('iKaMetApp.process.lastStatus')}
                id="process-lastStatus"
                name="lastStatus"
                data-cy="lastStatus"
                type="text"
              />
              <ValidatedField
                label={translate('iKaMetApp.process.lastDescription')}
                id="process-lastDescription"
                name="lastDescription"
                data-cy="lastDescription"
                type="text"
              />
              <ValidatedField
                label={translate('iKaMetApp.process.editBy')}
                id="process-editBy"
                name="editBy"
                data-cy="editBy"
              type="select"

            >
              <option value={account.login} key={account.login}>
                {account.login}
              </option>
            </ValidatedField>

              <ValidatedField
                id="process-candidate"
                name="candidate"
                data-cy="candidate"
                label={translate('iKaMetApp.process.candidate')}
                type="select"
              >

                {candidates
                  ? candidates.map(otherEntity => (
                    <option value={otherEntity.id} key={otherEntity.id}>
                      {otherEntity.firstName}{otherEntity.lastName}
                    </option>
                  ))
                  : null}
              </ValidatedField>

              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/process" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ProcessUpdate;
