import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './process.reducer';
import { IProcess } from 'app/shared/model/process.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import {getEntities as getTechnicalInterviews} from "app/entities/technical-interview/technical-interview.reducer";
import {getEntities as getHrInterviews} from "app/entities/hr-interview/hr-interview.reducer";
import Select from "react-select";


export const Process = (props: RouteComponentProps<{ url: string }>) => {










  const dispatch = useAppDispatch();

  const processList = useAppSelector(state => state.process.entities);
  const loading = useAppSelector(state => state.process.loading);
  const technicalInterviews = useAppSelector(state => state.technicalInterview.entities);
  const hrInterviews = useAppSelector(state => state.hrInterview.entities);


  useEffect(() => {
    dispatch(getEntities({}));
    dispatch(getTechnicalInterviews({}));
    dispatch(getHrInterviews({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>

      <h2 id="process-heading" data-cy="ProcessHeading">
        <Translate contentKey="iKaMetApp.process.home.title">Processes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="iKaMetApp.process.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="iKaMetApp.process.home.createLabel">Create new Process</Translate>
          </Link>
        </div>
      </h2>
      <div style={{width :'800px'}}>
        <Select
          placeholder="Select Candidate"
          closeMenuOnSelect={false}
          isMulti

          className={"Select"}
          classNamePrefix={"Select"}

          maxMenuHeight={300}
        />
      </div>
      <div className="table-responsive">
        {processList && processList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="iKaMetApp.process.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.process.candidate">Candidate</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.process.department">Department</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.process.technicalIndicators">Technical Indicators</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.process.experience">Experience</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.process.position">Position</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.process.salaryExpectation">Salary Expectation</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.process.possibleAssignmnet">Possible Assignmnet</Translate>
                </th>
                <th>
                  <p style={{textAlign:'center'}}>Human Resources Interview</p>

                </th>
                <th>
                  <p style={{textAlign:'center'}}>Technical Interview</p>

                </th>

                <th>
                  <Translate contentKey="iKaMetApp.process.lastStatus">Last Status</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.process.lastDescription">Last Description</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.process.editBy">Edit By</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.process.pdate">Pdate</Translate>
                </th>

                <th />
              </tr>
            </thead>
            <tbody>
              {processList.filter(ide=>ide.lastStatus !=="işe alındı" ).map((process, k) => (
                <tr key={`entity-${k}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${process.id}`} color="link" size="sm">
                      {process.id}
                    </Button>
                  </td>
                  <td>{process.candidate ? <Link to={`candidate/${process.candidate.id}`}>{process.candidate.firstName}{process.candidate.lastName}</Link> : ''}</td>
                  <td>{process.department}</td>
                  <td>{process.technicalIndicators}</td>
                  <td>{process.experience}</td>
                  <td>{process.position}</td>
                  <td>{process.salaryExpectation}</td>
                  <td>{process.possibleAssignmnet}</td>


                  <td>

                    <div className="table-responsive">
                      {hrInterviews && hrInterviews.length > 0 ? (
                        <Table responsive>
                          <thead>
                          <tr>
                            <th>
                              <Translate contentKey="iKaMetApp.hrInterview.id">ID</Translate>
                            </th>

                            <th>
                              <Translate contentKey="iKaMetApp.hrInterview.score">Score</Translate>
                            </th>
                            <th>
                              <Translate contentKey="iKaMetApp.hrInterview.ikStatus">Ik Status</Translate>
                            </th>
                            <th>
                              <Translate contentKey="iKaMetApp.hrInterview.notes">Notes</Translate>
                            </th>
                            <th>
                              <Translate contentKey="iKaMetApp.hrInterview.editBy">Edit By</Translate>
                            </th>
                            <th>
                              <Translate contentKey="iKaMetApp.hrInterview.date">Date</Translate>
                            </th>
                            <th />
                          </tr>
                          </thead>
                          <tbody>
                          {hrInterviews.filter(ide=>ide.process.id === process.id).map((hrInterview, h) => (
                            <tr key={`entity-${h}`} data-cy="entityTable">
                              <td>
                                <Button tag={Link} to={`${match.url}/${hrInterview.id}`} color="link" size="sm">
                                  {hrInterview.id}
                                </Button>
                              </td>


                              <td>{hrInterview.score}</td>
                              <td>{hrInterview.ikStatus}</td>
                              <td>{hrInterview.notes}</td>
                              <td>{hrInterview.editBy}</td>
                              <td>{hrInterview.date ? <TextFormat type="date" value={hrInterview.date} format={APP_DATE_FORMAT} /> : null}</td>

                            </tr>
                          ))}
                          </tbody>
                        </Table>
                      ) : (
                        !loading && (
                          <div className="alert alert-warning">
                            <Translate contentKey="iKaMetApp.hrInterview.home.notFound">No Hr Interviews found</Translate>
                          </div>
                        )
                      )}
                    </div>




                  </td>




                  <td>


                    <div className="table-responsive">
                      {technicalInterviews && technicalInterviews.length > 0 ? (
                        <Table responsive>
                          <thead>
                          <tr>
                            <th>
                              <Translate contentKey="iKaMetApp.technicalInterview.id">ID</Translate>
                            </th>


                            <th>
                              <Translate contentKey="iKaMetApp.technicalInterview.score">Score</Translate>
                            </th>
                            <th>
                              <Translate contentKey="iKaMetApp.technicalInterview.technicalStatus" >Technical Status</Translate>
                            </th>
                            <th>
                              <Translate contentKey="iKaMetApp.technicalInterview.notes">Notes</Translate>
                            </th>
                            <th>
                              <Translate contentKey="iKaMetApp.technicalInterview.editBy">Edit By</Translate>
                            </th>
                            <th>
                              <Translate contentKey="iKaMetApp.technicalInterview.date">Date</Translate>
                            </th>
                            <th />
                          </tr>
                          </thead>
                          <tbody>
                          {technicalInterviews.filter(ide=>ide.process.id === process.id).map((technicalInterview, i) => (
                            <tr key={`entity-${i}`} data-cy="entityTable">
                              <td>
                                <Button tag={Link} to={`${match.url}/${technicalInterview.id}`} color="link" size="sm">
                                  {technicalInterview.id}
                                </Button>
                              </td>


                              <td>
                                {technicalInterview.score}

                              </td>
                              <td>
                                {technicalInterview.technicalStatus}
                              </td>
                              <td>
                                {technicalInterview.notes}
                              </td>
                              <td>
                                {technicalInterview.editBy}
                              </td>
                              <td>{technicalInterview.date}
                              </td>
                            </tr>
                          ))}
                          </tbody>
                        </Table>
                      ) : (
                        !loading && (
                          <div className="alert alert-warning">
                            <Translate contentKey="iKaMetApp.technicalInterview.home.notFound">No Technical Interviews found</Translate>
                          </div>
                        )
                      )}

                    </div>
                  </td>








                  <td>{process.lastStatus}</td>
                  <td>{process.lastDescription}</td>
                  <td>{process.editBy}</td>
                  <td>{process.pdate ? <TextFormat type="date" value={process.pdate} format={APP_DATE_FORMAT} /> : null}</td>

                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${process.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${process.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${process.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="iKaMetApp.process.home.notFound">No Processes found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Process;
