import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import {Button,  Input,  Table} from 'reactstrap';
import {openFile, Translate} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getEntities } from './candidate.reducer';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import {getEntities as getFiles} from "app/entities/file/file.reducer";


export const Candidate = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const candidateList = useAppSelector(state => state.candidate.entities);
  const loading = useAppSelector(state => state.candidate.loading);
  const files = useAppSelector(state => state.file.entities);
  const [filter, setFilter] = useState('');
  useEffect(() => {
    dispatch(getEntities({}));
    dispatch(getFiles({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;
  const changeFilter = evt => setFilter(evt.target.value);

  const filterFn = l => l.firstName.toUpperCase().includes(filter.toUpperCase());




  return (
    <div>
      <h2 id="candidate-heading" data-cy="CandidateHeading">
        <Translate contentKey="iKaMetApp.candidate.home.title">Candidates</Translate>


        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="iKaMetApp.candidate.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="iKaMetApp.candidate.home.createLabel">Create new Candidate</Translate>
          </Link>
        </div>
        <Input placeholder={"Search Candidate FirstName"} value={filter}  style={{width :'1000px'}} onChange={changeFilter}  type="search"  name="search" id="search"   />

      </h2>
      <div className="table-responsive">
        {candidateList && candidateList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="iKaMetApp.candidate.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.candidate.firstName">First Name</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.candidate.lastName">Last Name</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.candidate.university">University</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.candidate.graduationYear">Graduation Year</Translate>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.candidate.gpa">Gpa</Translate>
                </th>
                <th>
                  <p style={{textAlign:'center'}}>Cv</p>
                </th>
                <th>
                  <Translate contentKey="iKaMetApp.candidate.editBy">Edit By</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {candidateList.filter(filterFn ).map((candidate, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${candidate.id}`} color="link" size="sm">
                      {candidate.id}
                    </Button>
                  </td>
                  <td>{candidate.firstName}</td>
                  <td>{candidate.lastName}</td>
                  <td>{candidate.university}</td>
                  <td>{candidate.graduationYear}</td>
                  <td>{candidate.gpa}</td>

                  <td>
                    <div className="table-responsive">
                      {files && files.length > 0 ? (
                        <Table responsive>
                          <tbody>
                          {files.filter(ide=> ide.candidate.id === candidate.id).map((file, f) => (
                            <tr key={`entity-${f}`} data-cy="entityTable">
                              <td>
                                {file.data ? (
                                  <div>
                                    {file.dataContentType ? (
                                      <a onClick={openFile(file.dataContentType, file.data)}>
                                        <Translate contentKey="entity.action.open">Open</Translate>
                                        &nbsp;
                                      </a>
                                    ) : null}
                                    <span>
                          {file.candidate.firstName}{"_"}{file.candidate.lastName}{"_"}{"cv"}
                        </span>
                                  </div>
                                ) : null}
                              </td>
                            </tr>
                          ))}
                          </tbody>
                        </Table>
                      ) : (
                        !loading && (
                          <div>{''}</div>
                        )
                      )}
                    </div>
                  </td>
                  <td>{candidate.editBy}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${candidate.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${candidate.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${candidate.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="iKaMetApp.candidate.home.notFound">No Candidates found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Candidate;
